package com.apirest.users.services;

import com.apirest.users.dto.PhoneDto;
import com.apirest.users.dto.UpdateUserDto;
import com.apirest.users.dto.UserDto;
import com.apirest.users.exception.DuplicateEmailException;
import com.apirest.users.model.Phone;
import com.apirest.users.model.User;
import com.apirest.users.repository.PhoneRepository;
import com.apirest.users.repository.UserRepository;
import com.apirest.users.validation.PasswordValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PhoneRepository phoneRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private PasswordValidator passwordValidator;

    private UserDto newUserDto() {
        UserDto dto = new UserDto();
        dto.setName("Jonathan Diaz");
        dto.setEmail("testmail@gmail.com");
        dto.setPassword("Abcdef12");
        PhoneDto p = new PhoneDto();
        p.setNumber("1234567");
        p.setCityCode("2");
        p.setCountryCode("56");
        dto.setPhones(Collections.singletonList(p));
        return dto;
    }

    @Test
    void createUser_ok() {
        UserDto dto = newUserDto();

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        doNothing().when(passwordValidator).validate(dto.getPassword());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.createUser(dto);

        assertEquals("Jonathan Diaz", user.getName());
        assertEquals("testmail@gmail.com", user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertNotNull(user.getLastLoginAt());
        assertNotNull(user.getToken());
        assertTrue(user.isActive());
        assertEquals(1, user.getPhones().size());
    }

    @Test
    void createUser_emailAlreadyExists() {
        UserDto dto = newUserDto();
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));
        doNothing().when(passwordValidator).validate(anyString());

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(dto));
    }

    @Test
    void createUser_invalidPassword() {
        UserDto dto = newUserDto();

        doThrow(new IllegalArgumentException("La contraseña no cumple el formato requerido"))
                .when(passwordValidator).validate(anyString());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto));
        assertTrue(ex.getMessage().toLowerCase().contains("contraseña"));

        verifyNoInteractions(userRepository, passwordEncoder, phoneRepository);
    }

    @Test
    void updateUser_ok() {
        UUID id = UUID.randomUUID();
        UpdateUserDto update = new UpdateUserDto();
        update.setName("Updated User");
        update.setPassword("Abcdef12");
        PhoneDto p = new PhoneDto();
        p.setNumber("9999999");
        p.setCityCode("2");
        p.setCountryCode("56");
        update.setPhones(Collections.singletonList(p));

        User existing = new User();
        existing.setId(id);
        existing.setEmail("testmail@gmail.com");
        existing.setName("Old");
        existing.setPassword("oldEncoded");
        existing.setPhones(new ArrayList<>());

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(passwordValidator).validate(update.getPassword());
        when(passwordEncoder.encode(update.getPassword())).thenReturn("newEncoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.updateUser(id, update);

        assertEquals("Updated User", user.getName());
        assertEquals("newEncoded", user.getPassword());
        assertNotNull(user.getUpdatedAt());
        assertEquals(1, user.getPhones().size());
        assertEquals("9999999", user.getPhones().get(0).getNumber());
        assertEquals("2", user.getPhones().get(0).getCityCode());
        assertEquals("56", user.getPhones().get(0).getCountryCode());
    }

    @Test
    void updateUser_notFound() {
        UUID id = UUID.randomUUID();
        UpdateUserDto update = new UpdateUserDto();
        update.setEmail("none@test.com");

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.updateUser(id, update));
    }

    @Test
    void getAllUsers_ok() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(new User(), new User()));
        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
    }

    @Test
    void getUserByMail_ok() {
        User u = new User();
        u.setEmail("x@test.com");
        when(userRepository.findByEmail("x@test.com")).thenReturn(Optional.of(u));
        Optional<User> got = userService.getUserByMail("x@test.com");
        assertTrue(got.isPresent());
        assertEquals("x@test.com", got.get().getEmail());
    }

    @Test
    void deleteUser_ok() {
        UUID id = UUID.randomUUID();
        doNothing().when(userRepository).deleteById(id);
        userService.deleteUser(id);
        verify(userRepository, times(1)).deleteById(id);
    }
}