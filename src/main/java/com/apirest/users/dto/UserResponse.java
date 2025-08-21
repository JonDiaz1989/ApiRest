package com.apirest.users.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class UserResponse {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("creado")
    private LocalDateTime createdAt;
    @JsonProperty("modificado")
    private LocalDateTime updatedAt;
    @JsonProperty("ultimoLogin")
    private LocalDateTime lastLoginAt;
    @JsonProperty("token")
    private String token;
    @JsonProperty("activo")
    private boolean active;

    @JsonProperty("nombre")
    private String name;
    @JsonProperty("correo")
    private String email;
    @JsonProperty("telefonos")
    private List<PhoneDto> phones;

    public UserResponse(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt,
                        LocalDateTime lastLoginAt, String token, boolean active,
                        String name, String email, List<PhoneDto> phones) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLoginAt = lastLoginAt;
        this.token = token;
        this.active = active;
        this.name = name;
        this.email = email;
        this.phones = phones;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public String getToken() {
        return token;
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<PhoneDto> getPhones() {
        return phones;
    }
}