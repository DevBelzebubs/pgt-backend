package com.portable.microservices.ms_administration.iam.domain.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.portable.microservices.ms_administration.iam.domain.model.User;

public interface UserPersistencePortOut {
    User save(User user);
    Optional<User> findById(String id);
    Optional<User> findByUuid(UUID uuid);
    Optional<User> findByDni(String dni);
    List<User> findAll();
    void deleteById(String id);
}
