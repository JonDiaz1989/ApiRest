package com.apirest.users.services;

import com.apirest.users.dto.PhoneDto;
import com.apirest.users.dto.UpdateUserDto;
import com.apirest.users.dto.UserDto;
import com.apirest.users.model.Phone;
import com.apirest.users.model.User;
import com.apirest.users.repository.PhoneRepository;
import com.apirest.users.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User createUser(UserDto userDto) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (!emailValidator.isValid(userDto.getEmail())) {
            throw new RuntimeException("El correo no es válido");
        }
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("El correo ya registrado");
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setCreated(new Date());
        user.setModified(new Date());
        user.setLastLogin(new Date());
        user.setToken(generateToken(user));
        user.setActive(true);

        List<Phone> phones = new ArrayList<>();
        for (PhoneDto phoneDto : userDto.getPhones()) {
            Phone phone = new Phone();
            phone.setNumber(phoneDto.getNumber());
            phone.setCityCode(phoneDto.getCitycode());
            phone.setCountryCode(phoneDto.getContrycode());
            phone.setUser(user);
            phones.add(phone);
        }
        user.setPhones(phones);

        return userRepository.save(user);

    }

    @Transactional
    public User updateUser(UpdateUserDto updateUserDto) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (!emailValidator.isValid(updateUserDto.getEmail())) {
            throw new RuntimeException("El correo no es válido");
        }
        Optional<User> optionalUser = userRepository.findByEmail(updateUserDto.getEmail());
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        User user = optionalUser.get();
        if (updateUserDto.getName() != null) {
            user.setName(updateUserDto.getName());
        }
        if (updateUserDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
        }
        user.setModified(new Date());
        if (user.getPhones() == null) {
            user.setPhones(new ArrayList<>());
        } else {
            user.getPhones().clear();
        }
        user = userRepository.save(user);
        phoneRepository.deleteByUser(user);
        List<Phone> newPhones = new ArrayList<>();
        if (updateUserDto.getPhones() != null) {
            for (PhoneDto phoneDto : updateUserDto.getPhones()) {
                Phone phone = new Phone();
                phone.setNumber(phoneDto.getNumber());
                phone.setCityCode(phoneDto.getCitycode());
                phone.setCountryCode(phoneDto.getContrycode());
                phone.setUser(user);
                phone = phoneRepository.save(phone);
                newPhones.add(phone);
            }
        }

        user.setPhones(newPhones);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByMail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> userExist(Long id) {
        return userRepository.findById(id);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UUID generateToken(User user) {
        return UUID.randomUUID();
    }
}
