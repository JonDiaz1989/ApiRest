package com.apirest.users.controller;

import com.apirest.users.common.api.ApiResponse;
import com.apirest.users.dto.PhoneDto;
import com.apirest.users.dto.UpdateUserDto;
import com.apirest.users.dto.UserDto;
import com.apirest.users.dto.UserResponse;
import com.apirest.users.model.Phone;
import com.apirest.users.model.User;
import com.apirest.users.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class UsersControllerV1 {

    private final UserService userService;

    public UsersControllerV1(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserResponse>> create(@RequestBody UserDto req) {
        User saved = userService.createUser(req);
        return ResponseEntity
                .created(URI.create("/api/v1/users/" + saved.getId()))
                .body(ApiResponse.ok(toResponse(saved)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        return userService.userExist(id)
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(ApiResponse.ok(toResponse(u))))
                .orElseGet(() -> ResponseEntity.status(404).body(Collections.singletonMap("mensaje", "Recurso no encontrado")));
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> list() {
        return ApiResponse.ok(
                userService.getAllUsers().stream().map(this::toResponse).collect(Collectors.toList())
        );
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<UserResponse> put(@PathVariable UUID id, @RequestBody UpdateUserDto req) {
        User current = userService.userExist(id).orElseThrow(NoSuchElementException::new);
        if (req.getEmail() == null || req.getEmail().isBlank()) {
            req.setEmail(current.getEmail());
        }
        User updated = userService.updateUser(req);
        return ApiResponse.ok(toResponse(updated));
    }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<UserResponse> patch(@PathVariable UUID id, @RequestBody Map<String, Object> fields) {
        User current = userService.userExist(id).orElseThrow(NoSuchElementException::new);
        UpdateUserDto req = new UpdateUserDto();
        req.setEmail(current.getEmail());
        if (fields.containsKey("nombre")) req.setName(String.valueOf(fields.get("nombre")));
        else if (fields.containsKey("name")) req.setName(String.valueOf(fields.get("name")));
        if (fields.containsKey("contraseña")) req.setPassword(String.valueOf(fields.get("contraseña")));
        else if (fields.containsKey("password")) req.setPassword(String.valueOf(fields.get("password")));
        if (fields.containsKey("telefonos") || fields.containsKey("phones")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> src = (List<Map<String, Object>>) (
                    fields.containsKey("telefonos") ? fields.get("telefonos") : fields.get("phones")
            );
            List<PhoneDto> phones = src.stream().map(m -> {
                PhoneDto p = new PhoneDto();
                Object n = m.getOrDefault("numero", m.get("number"));
                Object cc = m.getOrDefault("codigoCiudad", m.getOrDefault("cityCode", m.get("citycode")));
                Object pc = m.getOrDefault("codigoPais", m.getOrDefault("countryCode", m.get("contrycode")));
                p.setNumber(n == null ? null : String.valueOf(n));
                p.setCityCode(cc == null ? null : String.valueOf(cc));
                p.setCountryCode(pc == null ? null : String.valueOf(pc));
                return p;
            }).collect(Collectors.toList());
            req.setPhones(phones);
        }
        User updated = userService.updateUser(req);
        return ApiResponse.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.userExist(id).orElseThrow(NoSuchElementException::new);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private UserResponse toResponse(User e) {
        List<PhoneDto> phones = Optional.ofNullable(e.getPhones()).orElseGet(List::of)
                .stream().map(this::toDto).collect(Collectors.toList());
        return new UserResponse(
                e.getId(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getLastLoginAt(),
                e.getToken(),
                e.isActive(),
                e.getName(),
                e.getEmail(),
                phones
        );
    }

    private PhoneDto toDto(Phone p) {
        PhoneDto dto = new PhoneDto();
        dto.setNumber(p.getNumber());
        dto.setCityCode(p.getCityCode());
        dto.setCountryCode(p.getCountryCode());
        return dto;
    }
}