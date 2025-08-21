package com.apirest.users.common.api.error;

import com.apirest.users.exception.DuplicateEmailException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApiErrorHandler {
    public record ErrorMsg(String mensaje) {}

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorMsg> duplicate() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg("El correo ya está registrado"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMsg> bad(MethodArgumentNotValidException ex) {
        var msg = ex.getBindingResult().getFieldErrors().isEmpty()
                ? "Solicitud inválida"
                : ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg(msg));
    }

    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<ErrorMsg> notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMsg("Recurso no encontrado"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMsg> illegal(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMsg(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMsg> unexpected() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMsg("Error interno"));
    }
}