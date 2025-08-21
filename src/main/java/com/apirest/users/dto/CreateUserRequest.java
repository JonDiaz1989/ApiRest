package com.apirest.users.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class CreateUserRequest {
    @JsonProperty("nombre")
    @JsonAlias("name")
    @NotBlank
    private String name;

    @JsonProperty("correo")
    @JsonAlias("email")
    @NotBlank
    @Email
    private String email;

    @JsonProperty("contraseña")
    @JsonAlias("password")
    @NotBlank
    private String password;

    @JsonProperty("telefonos")
    @JsonAlias("phones")
    private List<PhoneDto> phones;

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }
}
