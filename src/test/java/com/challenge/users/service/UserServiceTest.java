package com.challenge.users.service;

import com.challenge.users.dto.UserRequest;
import com.challenge.users.dto.UserResponse;
import com.challenge.users.repository.UserRepository;
import com.challenge.users.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(userService, "passwordRegex", "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

    @Test
    void registerUser_Success() {
        // Preparar
        UserRequest request = UserRequest.builder()
                .name("Test User")
                .email("test@dominio.cl")
                .password("password123")
                .build();

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(tokenProvider.generateToken(any())).thenReturn("mock-token");

        // Ejecutar
        UserResponse response = userService.registerUser(request);

        // Verificar
        assertNotNull(response);
        assertEquals("mock-token", response.getToken());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void registerUser_EmailAlreadyExists_ThrowsException() {
        // Preparar
        UserRequest request = UserRequest.builder()
                .name("Test User")
                .email("existing@dominio.cl")
                .password("password123")
                .build();

        when(userRepository.existsByEmail(any())).thenReturn(true);

        // Ejecutar y Verificar
        Exception exception = assertThrows(RuntimeException.class, () -> userService.registerUser(request));
        assertEquals("El correo ya registrado", exception.getMessage());
    }

    @Test
    void registerUser_InvalidEmail_ThrowsException() {
        // Preparar
        UserRequest request = UserRequest.builder()
                .name("Test User")
                .email("test@other.com")
                .password("password123")
                .build();

        // Ejecutar y Verificar
        Exception exception = assertThrows(RuntimeException.class, () -> userService.registerUser(request));
        assertTrue(exception.getMessage().contains("Formato de correo inválido"));
    }

    @Test
    void registerUser_InvalidPassword_ThrowsException() {
        // Preparar
        UserRequest request = UserRequest.builder()
                .name("Test User")
                .email("test@dominio.cl")
                .password("short")
                .build();

        // Ejecutar y Verificar
        Exception exception = assertThrows(RuntimeException.class, () -> userService.registerUser(request));
        assertEquals("Formato de contraseña inválido", exception.getMessage());
    }
}
