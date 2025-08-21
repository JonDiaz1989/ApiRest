package com.apirest.users.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PhoneTest {

    private Phone phone;

    @BeforeEach
    void setUp() {
        phone = new Phone();
    }

    @Test
    void shouldSetAndGetId() {
        UUID id = UUID.randomUUID();
        phone.setId(id);
        assertEquals(id, phone.getId());
    }

    @Test
    void shouldSetAndGetNumber() {
        String number = "1234567890";
        phone.setNumber(number);
        assertEquals(number, phone.getNumber()); // <- aquí estaba el error
    }

    @Test
    void shouldSetAndGetCityCode() {
        String cityCode = "01";
        phone.setCityCode(cityCode);
        assertEquals(cityCode, phone.getCityCode());
    }

    @Test
    void shouldSetAndGetCountryCode() {
        String countryCode = "56";
        phone.setCountryCode(countryCode);
        assertEquals(countryCode, phone.getCountryCode());
    }

    @Test
    void shouldSetAndGetUser() {
        User user = new User();
        user.setEmail("testmail@gmail.com");
        phone.setUser(user);
        assertEquals(user, phone.getUser());
    }
}