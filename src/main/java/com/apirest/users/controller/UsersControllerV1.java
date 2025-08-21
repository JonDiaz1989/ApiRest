package com.apirest.users.controller;

import com.apirest.users.common.api.ApiResponse;
import com.apirest.users.dto.UpdateUserDto;
import com.apirest.users.dto.UserDto;
import com.apirest.users.dto.UserResponse;
import com.apirest.users.model.User;
import com.apirest.users.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.UUID;
import java.util.stream.Collectors;

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
                .body(ApiResponse.ok(UserResponse.fromEntity(saved)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        return userService.userExist(id)
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(ApiResponse.ok(UserResponse.fromEntity(u))))
                .orElseGet(() -> ResponseEntity.status(404).body(Collections.singletonMap("mensaje", "Recurso no encontrado")));
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> list() {
        return ApiResponse.ok(
                userService.getAllUsers().stream().map(UserResponse::fromEntity).collect(Collectors.toList())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserDto body) {

        if (userService.userExist(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Recurso no encontrado"));
        }

        User updated = userService.updateUser(id, body);
        URI location = URI.create("/api/v1/users/" + updated.getId());
        return ResponseEntity.ok(ApiResponse.success(UserResponse.fromEntity(updated)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> patch(
            @PathVariable UUID id,
            @RequestBody UpdateUserDto body) {

        if (userService.userExist(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Recurso no encontrado"));
        }

        User updated = userService.updateUser(id, body);
        return ResponseEntity.ok(ApiResponse.success(UserResponse.fromEntity(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.userExist(id).orElseThrow(NoSuchElementException::new);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}