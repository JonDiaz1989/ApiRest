package com.apirest.users.controller;

import com.apirest.users.dto.UpdateUserDto;
import com.apirest.users.dto.UserDto;
import com.apirest.users.model.User;
import com.apirest.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        try {
            User user = userService.createUser(userDto);
            return ResponseEntity.status(201).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", e.getMessage()));
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getUserByMail/{mail}")
    public ResponseEntity<?> getUserByMail(@PathVariable String mail) {
        Optional<User> user = userService.getUserByMail(mail);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(204).build();
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDto updateUserDto) {
        try {
            User user = userService.updateUser(updateUserDto);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", e.getMessage()));
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.userExist(id);
        if (!user.isPresent()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("mensaje", "No pudo ser borrado el usuario."));
        }
        userService.deleteUser(id);
        return ResponseEntity.ok(Collections.singletonMap("mensaje", "Usuario borrado satisfactoriamente"));
    }

}
