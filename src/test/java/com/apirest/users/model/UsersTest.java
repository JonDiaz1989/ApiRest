package com.apirest.users.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void id_set_get() {
        UUID id = UUID.randomUUID();
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    void name_set_get() {
        user.setName("Jonathan Diaz");
        assertEquals("Jonathan Diaz", user.getName());
    }

    @Test
    void email_set_get() {
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void password_set_get() {
        user.setPassword("encoded");
        assertEquals("encoded", user.getPassword());
    }

    @Test
    void timestamps_set_get() {
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setLastLoginAt(now);
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
        assertEquals(now, user.getLastLoginAt());
    }

    @Test
    void token_active_set_get() {
        user.setToken("token-123");
        user.setActive(true);
        assertEquals("token-123", user.getToken());
        assertTrue(user.isActive());
    }
}