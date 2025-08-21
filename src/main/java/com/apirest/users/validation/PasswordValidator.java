package com.apirest.users.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    @Value("${security.password.regex}") private String regex;
    public void validate(String pwd){
        if (pwd == null || !pwd.matches(regex)) {
            throw new IllegalArgumentException("La contraseña no cumple el formato requerido");
        }
    }
}