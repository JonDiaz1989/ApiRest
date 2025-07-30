package com.apirest.users.repository;

import com.apirest.users.model.Phone;
import com.apirest.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PhoneRepository extends JpaRepository<Phone, UUID> {
    void deleteByUser(User user);

    List<Phone> findAll();
}
