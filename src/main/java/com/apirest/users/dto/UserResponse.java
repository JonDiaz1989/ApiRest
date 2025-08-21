package com.apirest.users.dto;

import com.apirest.users.model.Phone;
import com.apirest.users.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class UserResponse {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("nombre")
    private final String nombre;

    @JsonProperty("correo")
    private final String correo;

    @JsonProperty("creado")
    private final LocalDateTime creado;

    @JsonProperty("modificado")
    private final LocalDateTime modificado;

    @JsonProperty("ultimoLogin")
    private final LocalDateTime ultimoLogin;

    @JsonProperty("token")
    private final String token;

    @JsonProperty("activo")
    private final boolean activo;

    @JsonProperty("telefonos")
    private final List<PhoneResponse> telefonos;

    private UserResponse(UUID id,
                         String nombre,
                         String correo,
                         LocalDateTime creado,
                         LocalDateTime modificado,
                         LocalDateTime ultimoLogin,
                         String token,
                         boolean activo,
                         List<PhoneResponse> telefonos) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.creado = creado;
        this.modificado = modificado;
        this.ultimoLogin = ultimoLogin;
        this.token = token;
        this.activo = activo;
        this.telefonos = telefonos;
    }

    public static UserResponse fromEntity(User u) {
        List<PhoneResponse> phones = (u.getPhones() == null) ? Collections.emptyList()
                : u.getPhones().stream().map(PhoneResponse::fromEntity).collect(Collectors.toList());

        return new UserResponse(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getCreatedAt(),
                u.getUpdatedAt(),
                u.getLastLoginAt(),
                u.getToken(),
                u.isActive(),
                phones
        );
    }

    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public LocalDateTime getCreado() { return creado; }
    public LocalDateTime getModificado() { return modificado; }
    public LocalDateTime getUltimoLogin() { return ultimoLogin; }
    public String getToken() { return token; }
    public boolean isActivo() { return activo; }
    public List<PhoneResponse> getTelefonos() { return telefonos; }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class PhoneResponse {

        @JsonProperty("numero")
        private final String numero;

        @JsonProperty("codigoCiudad")
        private final String codigoCiudad;

        @JsonProperty("codigoPais")
        private final String codigoPais;

        private PhoneResponse(String numero, String codigoCiudad, String codigoPais) {
            this.numero = numero;
            this.codigoCiudad = codigoCiudad;
            this.codigoPais = codigoPais;
        }

        public static PhoneResponse fromEntity(Phone p) {
            return new PhoneResponse(
                    p.getNumber(),
                    p.getCityCode(),
                    p.getCountryCode()
            );
        }

        public String getNumero() { return numero; }
        public String getCodigoCiudad() { return codigoCiudad; }
        public String getCodigoPais() { return codigoPais; }
    }
}