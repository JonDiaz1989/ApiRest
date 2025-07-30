package com.apirest.users.controller;

import com.apirest.users.dto.UpdateUserDto;
import com.apirest.users.dto.UserDto;
import com.apirest.users.model.User;
import com.apirest.users.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("testmail@gmail.com");
        userDto.setName("Jonathan Diaz");
        userDto.setPassword("password");

        User user = new User();
        user.setEmail("testmail@gmail.com");
        user.setName("Jonathan Diaz");
        user.setPassword("password");

        when(userService.createUser(any(UserDto.class))).thenReturn(user);

        mockMvc.perform(post("/users/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateUser() throws Exception {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setEmail("testmail@gmail.com");
        updateUserDto.setName("Updated User");
        updateUserDto.setPassword("newPassword");

        User user = new User();
        user.setEmail("testmail@gmail.com");
        user.setName("Updated User");
        user.setPassword("newEncodedPassword");

        UUID userId = UUID.randomUUID();
        when(userService.updateUser(updateUserDto)).thenReturn(user);

        mockMvc.perform(put("/users/updateUser", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllUsersSuccess() throws Exception {
        List<User> users = Arrays.asList(new User(), new User());

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users/getAllUsers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllUsersNoContent() throws Exception {
        List<User> users = Collections.emptyList();

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users/getAllUsers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setEmail("testmail@gmail.com");

        when(userService.getUserByMail(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/getUserByMail/{mail}", "testmail@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserByIdNoContent() throws Exception {
        when(userService.getUserByMail(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/getUserByMail/{mail}", "testmail@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUserSuccess() throws Exception {
        User user = new User();
        user.setId(1L);

        when(userService.userExist(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/users/deleteUser/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        when(userService.userExist(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/users/deleteUser/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
