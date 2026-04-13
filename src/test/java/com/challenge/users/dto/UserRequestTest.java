package com.challenge.users.dto;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserRequestTest {

    @Test
    void userRequest_BuilderTests() {
        // Act
        UserRequest request = UserRequest.builder()
                .name("Juan")
                .email("juan@dominio.cl")
                .password("hunter2")
                .phones(new ArrayList<>())
                .build();

        // Assert
        assertEquals("Juan", request.getName());
        assertEquals("juan@dominio.cl", request.getEmail());
        assertEquals("hunter2", request.getPassword());
        assertNotNull(request.getPhones());
    }

    @Test
    void phoneDTO_Tests() {
        // Act
        PhoneDTO dto = PhoneDTO.builder()
                .number("123456")
                .citycode("1")
                .contrycode("57")
                .build();

        // Assert
        assertEquals("123456", dto.getNumber());
        assertEquals("1", dto.getCitycode());
        assertEquals("57", dto.getContrycode());
    }
}
