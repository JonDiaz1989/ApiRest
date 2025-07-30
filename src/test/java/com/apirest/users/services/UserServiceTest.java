package com.apirest.users.services;

import com.apirest.users.dto.UpdateUserDto;
import com.apirest.users.dto.UserDto;
import com.apirest.users.model.User;
import com.apirest.users.repository.PhoneRepository;
import com.apirest.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    PhoneRepository phoneRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setEmail("testmail@gmail.com");
        userDto.setName("Jonathan Diaz");
        userDto.setPassword("password");
        userDto.setPhones(new ArrayList<>());

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User user = userService.createUser(userDto);

        assertEquals("Jonathan Diaz", user.getName());
        assertEquals("testmail@gmail.com", user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        assertNotNull(user.getCreated());
        assertNotNull(user.getModified());
        assertNotNull(user.getLastLogin());
        assertNotNull(user.getToken());
        assertTrue(user.isActive());
    }

    @Test
    public void testUpdateUser() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setEmail("testmail@gmail.com");
        updateUserDto.setName("Updated User");
        updateUserDto.setPassword("newPassword");
        updateUserDto.setPhones(new ArrayList<>());

        User existingUser = new User();
        existingUser.setEmail("testmail@gmail.com");
        existingUser.setName("Jonathan Diaz");
        existingUser.setPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User user = userService.updateUser(updateUserDto);

        assertEquals("Updated User", user.getName());
        assertEquals("testmail@gmail.com", user.getEmail());
        assertEquals("newEncodedPassword", user.getPassword());
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setEmail("testmail@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByMail("testmail@gmail.com");

        assertTrue(result.isPresent());
        assertEquals("testmail@gmail.com", result.get().getEmail());
    }

    @Test
    public void testDeleteUser() {
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testCreateUser_EmailAlreadyExists() {
        UserDto userDto = new UserDto();
        userDto.setEmail("testmail@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> userService.createUser(userDto));
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setEmail("testmail@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(updateUserDto));
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByMail("testmail@gmail.com");

        assertFalse(result.isPresent());
    }
}