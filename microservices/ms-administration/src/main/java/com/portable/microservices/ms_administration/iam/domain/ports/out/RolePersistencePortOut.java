package com.portable.microservices.ms_administration.iam.domain.ports.out;

import java.util.List;
import java.util.Optional;

import com.portable.microservices.ms_administration.iam.domain.model.Role;

public interface RolePersistencePortOut {
    Role save(Role role);
    Optional<Role> findByName(String name);
    List<Role> findAll();
}
