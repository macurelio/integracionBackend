package com.challenge.users.service;

import com.challenge.users.dto.PhoneDTO;
import com.challenge.users.dto.UserRequest;
import com.challenge.users.dto.UserResponse;
import com.challenge.users.model.Phone;
import com.challenge.users.model.User;
import com.challenge.users.repository.UserRepository;
import com.challenge.users.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service class for handling user-related business logic and validations.
 */
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    @Value("${app.validation.password-regex}")
    private String passwordRegex;

    /** Email regex requirement: aaaaaaa@dominio.cl */
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@dominio\\.cl$";

    public UserService(UserRepository userRepository, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Handles the business logic for registering a new user including validations and persistence.
     * 
     * @param request The registration request details.
     * @return The response containing generated UUID, timestamps, and token.
     * @throws RuntimeException if validation fails or email already exists.
     */
    @Transactional
    public UserResponse registerUser(UserRequest request) {
        log.debug("Starting registration process for email: {}", request.getEmail());
        
        validateEmail(request.getEmail());
        validatePassword(request.getPassword());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email {} is already registered", request.getEmail());
            throw new RuntimeException("El correo ya registrado");
        }

        String token = tokenProvider.generateToken(request.getEmail());
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword()) 
                .token(token)
                .lastLogin(now)
                .isActive(true)
                .build();

        if (request.getPhones() != null) {
            List<Phone> phones = request.getPhones().stream()
                    .map(phoneDto -> Phone.builder()
                            .number(phoneDto.getNumber())
                            .citycode(phoneDto.getCitycode())
                            .contrycode(phoneDto.getContrycode())
                            .user(user)
                            .build())
                    .collect(Collectors.toList());
            user.setPhones(phones);
        }

        log.debug("Saving user to database...");
        User savedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .created(savedUser.getCreated())
                .modified(savedUser.getModified())
                .last_login(savedUser.getLastLogin())
                .token(savedUser.getToken())
                .isactive(savedUser.isActive())
                .build();
    }

    /**
     * Validates that the email follows the required domain pattern.
     */
    private void validateEmail(String email) {
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            log.warn("Email validation failed for: {}", email);
            throw new RuntimeException("Formato de correo inválido (debe ser aaaaaaa@dominio.cl)");
        }
    }

    /**
     * Validates that the password meets the complexity requirements.
     */
    private void validatePassword(String password) {
        if (!Pattern.matches(passwordRegex, password)) {
            log.warn("Password complexity validation failed");
            throw new RuntimeException("Formato de contraseña inválido");
        }
    }
}
