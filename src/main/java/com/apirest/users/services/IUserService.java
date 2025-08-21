package com.apirest.users.services;

import com.apirest.users.dto.UpdateUserDto;
import com.apirest.users.dto.UserDto;
import com.apirest.users.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {
    User createUser(UserDto userDto);
    User updateUser(UUID id, UpdateUserDto dto);
    List<User> getAllUsers();
    Optional<User> getUserByMail(String email);
    Optional<User> userExist(UUID id);
    void deleteUser(UUID id);
}