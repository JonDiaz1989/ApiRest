package com.apirest.users.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DtoBindingTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void userDto_spanishJson_binds() throws Exception {
        String json = """
        {
          "nombre":"Angela",
          "correo":"angela@test.com",
          "contraseña":"Abcdef12",
          "telefonos":[{"numero":"1","codigoCiudad":"2","codigoPais":"56"}]
        }
        """;
        UserDto dto = mapper.readValue(json, UserDto.class);
        assertEquals("Angela", dto.getName());
        assertEquals("angela@test.com", dto.getEmail());
        assertEquals("Abcdef12", dto.getPassword());
        assertEquals(1, dto.getPhones().size());
        assertEquals("2", dto.getPhones().get(0).getCityCode());
        assertEquals("56", dto.getPhones().get(0).getCountryCode());
    }

    @Test
    void userDto_englishJson_binds() throws Exception {
        String json = """
        {
          "name":"Bob",
          "email":"bob@test.com",
          "password":"Abcdef12",
          "phones":[{"number":"7","citycode":"1","contrycode":"57"}]
        }
        """;
        UserDto dto = mapper.readValue(json, UserDto.class);
        assertEquals("Bob", dto.getName());
        assertEquals("bob@test.com", dto.getEmail());
        assertEquals("Abcdef12", dto.getPassword());
        assertEquals("1", dto.getPhones().get(0).getCityCode());
        assertEquals("57", dto.getPhones().get(0).getCountryCode());
    }
}