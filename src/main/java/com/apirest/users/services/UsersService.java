package com.apirest.users.services;

import com.apirest.users.dto.CreateUserRequest;
import com.apirest.users.dto.UpdateUserRequest;
import com.apirest.users.dto.UserResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UsersService {
    UserResponse create(CreateUserRequest req);

    Optional<UserResponse> findById(UUID id);

    List<UserResponse> findAll();

    UserResponse update(UUID id, UpdateUserRequest req);

    UserResponse patch(UUID id, Map<String, Object> fields);

    void delete(UUID id);
}
