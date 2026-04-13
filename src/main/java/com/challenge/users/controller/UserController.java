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
 * REST Controller for user registration and management.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Registration", description = "API for user creation and management")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user in the system.
     * 
     * @param request The user registration data.
     * @return The created user details with JWT token.
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        log.info("Received registration request for email: {}", request.getEmail());
        UserResponse response = userService.registerUser(request);
        log.info("User registered successfully with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
