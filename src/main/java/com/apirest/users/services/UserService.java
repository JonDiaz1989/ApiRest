package com.apirest.users.services;

import com.apirest.users.dto.PhoneDto;
import com.apirest.users.dto.UpdateUserDto;
import com.apirest.users.dto.UserDto;
import com.apirest.users.model.Phone;
import com.apirest.users.model.User;
import com.apirest.users.repository.PhoneRepository;
import com.apirest.users.repository.UserRepository;
import com.apirest.users.validation.PasswordValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private PasswordValidator passwordValidator;

    public User createUser(UserDto userDto) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (!emailValidator.isValid(userDto.getEmail())) {
            throw new RuntimeException("El correo no es válido");
        }

        passwordValidator.validate(userDto.getPassword());

        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            throw new com.apirest.users.exception.DuplicateEmailException();
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setLastLoginAt(now);

        user.setToken(generateToken());
        user.setActive(true);

        List<Phone> phones = new ArrayList<>();
        if (userDto.getPhones() != null) {
            for (PhoneDto phoneDto : userDto.getPhones()) {
                Phone phone = new Phone();
                phone.setNumber(phoneDto.getNumber());
                phone.setCityCode(phoneDto.getCityCode());
                phone.setCountryCode(phoneDto.getCountryCode());
                phone.setUser(user);
                phones.add(phone);
            }
        }
        user.setPhones(phones);

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UUID id, UpdateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(java.util.NoSuchElementException::new);

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            passwordValidator.validate(dto.getPassword());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        user.setUpdatedAt(java.time.LocalDateTime.now());

        if (dto.getPhones() != null) {
            if (user.getPhones() == null) {
                user.setPhones(new java.util.ArrayList<>());
            } else {
                user.getPhones().clear();
            }

            for (PhoneDto p : dto.getPhones()) {
                Phone phone = new Phone();
                phone.setNumber(p.getNumber());
                phone.setCityCode(p.getCityCode());
                phone.setCountryCode(p.getCountryCode());
                phone.setUser(user);
                user.getPhones().add(phone);
            }
        }

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByMail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> userExist(UUID id) {
        return userRepository.findById(id);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}