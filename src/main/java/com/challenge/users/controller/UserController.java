package com.challenge.users.controller;

import com.challenge.users.dto.UserRequest;
import com.challenge.users.dto.UserResponse;
import com.challenge.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controlador REST para el registro y gestión de usuarios.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Registro de Usuarios", description = "API para la creación y gestión de usuarios")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param request Datos de registro del usuario.
     * @return Detalles del usuario creado con el token JWT.
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        log.info("Solicitud de registro recibida para el correo: {}", request.getEmail());
        UserResponse response = userService.registerUser(request);
        log.info("Usuario registrado exitosamente con ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
