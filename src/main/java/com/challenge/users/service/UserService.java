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
 * Servicio para la lógica de negocio y validaciones relacionadas con usuarios.
 */
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    @Value("${app.validation.password-regex}")
    private String passwordRegex;

    /** Requisito de regex para email: aaaaaaa@dominio.cl */
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@dominio\\.cl$";

    public UserService(UserRepository userRepository, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Gestiona la lógica de negocio para registrar un nuevo usuario, incluyendo validaciones y persistencia.
     *
     * @param request Detalles de la solicitud de registro.
     * @return Respuesta con UUID generado, timestamps y token.
     * @throws RuntimeException si la validación falla o el correo ya existe.
     */
    @Transactional
    public UserResponse registerUser(UserRequest request) {
        log.debug("Iniciando proceso de registro para el correo: {}", request.getEmail());

        validateEmail(request.getEmail());
        validatePassword(request.getPassword());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registro fallido: El correo {} ya está registrado", request.getEmail());
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

        log.debug("Guardando usuario en la base de datos...");
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
     * Valida que el correo siga el patrón de dominio requerido.
     */
    private void validateEmail(String email) {
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            log.warn("Validación de correo fallida para: {}", email);
            throw new RuntimeException("Formato de correo inválido (debe ser aaaaaaa@dominio.cl)");
        }
    }

    /**
     * Valida que la contraseña cumpla con los requisitos de complejidad.
     */
    private void validatePassword(String password) {
        if (!Pattern.matches(passwordRegex, password)) {
            log.warn("Validación de complejidad de contraseña fallida");
            throw new RuntimeException("Formato de contraseña inválido");
        }
    }
}
