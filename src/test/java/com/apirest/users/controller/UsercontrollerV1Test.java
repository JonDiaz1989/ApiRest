package com.apirest.users.controller;

import com.apirest.users.model.Phone;
import com.apirest.users.model.User;
import com.apirest.users.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsersControllerV1.class)
@AutoConfigureMockMvc(addFilters = false)
class UsersControllerV1Test {

    @Autowired private MockMvc mockMvc;
    @MockBean private UserService userService;

    private User userEntity(UUID id) {
        User u = new User();
        u.setId(id);
        u.setName("Angela");
        u.setEmail("angela@test.com");
        u.setPassword("encoded");
        u.setCreatedAt(LocalDateTime.now());
        u.setUpdatedAt(LocalDateTime.now());
        u.setLastLoginAt(LocalDateTime.now());
        u.setToken(UUID.randomUUID().toString());
        u.setActive(true);
        Phone p = new Phone();
        p.setNumber("1"); p.setCityCode("2"); p.setCountryCode("56"); p.setUser(u);
        u.setPhones(Collections.singletonList(p));
        return u;
    }

    @Test
    void create_shouldReturn201() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.createUser(any())).thenReturn(userEntity(id));

        String body = """
        {
          "nombre":"Angela",
          "correo":"angela@test.com",
          "contraseña":"Abcdef12",
          "telefonos":[{"numero":"1","codigoCiudad":"2","codigoPais":"56"}]
        }
        """;

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/users/" + id))
                .andExpect(jsonPath("$.data.id", is(id.toString())))
                .andExpect(jsonPath("$.data.correo", is("angela@test.com")))
                .andExpect(jsonPath("$.data.telefonos[0].numero", is("1")));
    }

    @Test
    void get_shouldReturn200_whenExists() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.userExist(id)).thenReturn(Optional.of(userEntity(id)));

        mockMvc.perform(get("/api/v1/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(id.toString())));
    }

    @Test
    void get_shouldReturn404_whenNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.userExist(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/users/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje", is("Recurso no encontrado")));
    }

    @Test
    void put_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.userExist(id)).thenReturn(Optional.of(userEntity(id)));
        User updated = userEntity(id); updated.setName("Angela Actualizada");
        when(userService.updateUser(eq(id), any())).thenReturn(updated);

        String body = """
        {
          "nombre":"Angela Actualizada",
          "contraseña":"Abcdef12",
          "telefonos":[{"numero":"9","codigoCiudad":"2","codigoPais":"56"}]
        }
        """;

        mockMvc.perform(put("/api/v1/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nombre", is("Angela Actualizada")));
    }

    @Test
    void patch_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.userExist(id)).thenReturn(Optional.of(userEntity(id)));
        User patched = userEntity(id); patched.setName("Nombre Patch");
        when(userService.updateUser(eq(id), any())).thenReturn(patched);

        String body = "{\"nombre\":\"Nombre Patch\"}";

        mockMvc.perform(patch("/api/v1/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nombre", is("Nombre Patch")));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.userExist(id)).thenReturn(Optional.of(userEntity(id)));

        mockMvc.perform(delete("/api/v1/users/{id}", id))
                .andExpect(status().isNoContent());
    }
}