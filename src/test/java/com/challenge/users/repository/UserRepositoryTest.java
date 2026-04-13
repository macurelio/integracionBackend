package com.challenge.users.repository;

import com.challenge.users.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_ReturnsUser() {
        // Arrange
        User user = User.builder()
                .name("Juan")
                .email("juan@dominio.cl")
                .password("pass1234")
                .created(LocalDateTime.now())
                .build();
        userRepository.save(user);

        // Act
        Optional<User> found = userRepository.findByEmail("juan@dominio.cl");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getName());
    }

    @Test
    void existsByEmail_ReturnsTrue() {
        // Arrange
        User user = User.builder()
                .name("Juan")
                .email("exists@dominio.cl")
                .password("pass1234")
                .build();
        userRepository.save(user);

        // Act
        boolean exists = userRepository.existsByEmail("exists@dominio.cl");

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByEmail_NonExistent_ReturnsFalse() {
        // Act
        boolean exists = userRepository.existsByEmail("nonexistent@dominio.cl");

        // Assert
        assertFalse(exists);
    }
}
