package com.apirest.users.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void shouldSetAndGetId() {
        Long id = 1L;
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    void shouldSetAndGetName() {
        String name = "Jonathan Diaz";
        user.setName(name);
        assertEquals(name, user.getName());
    }

    @Test
    void shouldSetAndGetEmail() {
        String email = "test@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    void shouldSetAndGetPassword() {
        String password = "password";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    void shouldSetAndGetCreatedDate() {
        Date date = new Date();
        user.setCreated(date);
        assertEquals(date, user.getCreated());
    }

    @Test
    void shouldSetAndGetModifiedDate() {
        Date date = new Date();
        user.setModified(date);
        assertEquals(date, user.getModified());
    }

    @Test
    void shouldSetAndGetLastLoginDate() {
        Date date = new Date();
        user.setLastLogin(date);
        assertEquals(date, user.getLastLogin());
    }

    @Test
    void shouldSetAndGetToken() {
        UUID token = UUID.randomUUID();
        user.setToken(token);
        assertEquals(token, user.getToken());
    }

    @Test
    void shouldSetAndGetIsActive() {
        user.setActive(true);
        assertTrue(user.isActive());
    }
}
