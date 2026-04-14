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
        // Preparar
        User user = User.builder()
                .name("Juan")
                .email("juan@dominio.cl")
                .password("pass1234")
                .created(LocalDateTime.now())
                .build();
        userRepository.save(user);

        // Ejecutar
        Optional<User> found = userRepository.findByEmail("juan@dominio.cl");

        // Verificar
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getName());
    }

    @Test
    void existsByEmail_ReturnsTrue() {
        // Preparar
        User user = User.builder()
                .name("Juan")
                .email("exists@dominio.cl")
                .password("pass1234")
                .build();
        userRepository.save(user);

        // Ejecutar
        boolean exists = userRepository.existsByEmail("exists@dominio.cl");

        // Verificar
        assertTrue(exists);
    }

    @Test
    void existsByEmail_NonExistent_ReturnsFalse() {
        // Ejecutar
        boolean exists = userRepository.existsByEmail("nonexistent@dominio.cl");

        // Verificar
        assertFalse(exists);
    }
}
