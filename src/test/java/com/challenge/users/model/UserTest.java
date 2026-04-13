package com.challenge.users.model;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void userBuilder_AndLombok_WorkCorrectly() {
        // Arrange
        UUID id = UUID.randomUUID();
        Phone phone = Phone.builder().number("12345").build();

        // Act
        User user = User.builder()
                .id(id)
                .name("Juan")
                .email("juan@dominio.cl")
                .password("pass123")
                .phones(Collections.singletonList(phone))
                .isActive(true)
                .build();

        // Assert
        assertEquals(id, user.getId());
        assertEquals("Juan", user.getName());
        assertEquals(1, user.getPhones().size());
        assertTrue(user.isActive());
    }

    @Test
    void phone_Relationship_Test() {
        // Arrange
        User user = new User();
        Phone phone = Phone.builder()
                .number("999")
                .user(user)
                .build();

        // Assert
        assertNotNull(phone.getUser());
        assertEquals("999", phone.getNumber());
    }
}
