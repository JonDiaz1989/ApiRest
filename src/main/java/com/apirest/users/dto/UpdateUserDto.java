package com.apirest.users.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserDto {

    @JsonProperty("nombre")
    @JsonAlias("name")
    private String name;

    @JsonProperty("correo")
    @JsonAlias("email")
    @Email
    private String email;

    @JsonProperty("contraseña")
    @JsonAlias({"contrasena", "password"})
    private String password;

    @JsonProperty("telefonos")
    @JsonAlias("phones")
    private List<PhoneDto> phones;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PhoneDto> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDto> phones) {
        this.phones = phones;
    }
}