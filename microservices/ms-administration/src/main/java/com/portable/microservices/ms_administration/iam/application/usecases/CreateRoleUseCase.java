package com.portable.microservices.ms_administration.iam.application.usecases;

import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.ports.in.CreateRolePortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateRoleUseCase implements CreateRolePortIn {

    private final RolePersistencePortOut rolePersistence;

    @Override
    public Role execute(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del rol no puede estar vacío");
        }
        if (rolePersistence.findByName(name.trim()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + name);
        }
        return rolePersistence.save(new Role(null, name.trim(), null));
    }
}
