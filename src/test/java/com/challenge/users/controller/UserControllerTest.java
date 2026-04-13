package com.challenge.users.controller;

import com.challenge.users.dto.UserRequest;
import com.challenge.users.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_Conflict_ReturnsErrorMessage() throws Exception {
        // Arrange
        UserRequest request = UserRequest.builder()
                .name("Juan Rodriguez")
                .email("juan@dominio.cl")
                .password("hunter2password")
                .build();

        when(userService.registerUser(any())).thenThrow(new RuntimeException("El correo ya registrado"));

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El correo ya registrado"));
    }

    @Test
    void register_InvalidEmail_ReturnsErrorMessage() throws Exception {
        // Arrange
        UserRequest request = UserRequest.builder()
                .name("Juan Rodriguez")
                .email("juan@bad.com")
                .password("hunter2password")
                .build();

        when(userService.registerUser(any())).thenThrow(new RuntimeException("Formato de correo inválido (debe ser aaaaaaa@dominio.cl)"));

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Formato de correo inválido (debe ser aaaaaaa@dominio.cl)"));
    }

    @Test
    void register_InvalidPassword_ReturnsErrorMessage() throws Exception {
        // Arrange
        UserRequest request = UserRequest.builder()
                .name("Juan Rodriguez")
                .email("juan@dominio.cl")
                .password("short")
                .build();

        when(userService.registerUser(any())).thenThrow(new RuntimeException("Formato de contraseña inválido"));

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Formato de contraseña inválido"));
    }
}
