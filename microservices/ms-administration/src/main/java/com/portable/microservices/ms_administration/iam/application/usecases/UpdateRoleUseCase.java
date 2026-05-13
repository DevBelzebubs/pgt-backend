package com.portable.microservices.ms_administration.iam.application.usecases;

import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.ports.in.UpdateRolePortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateRoleUseCase implements UpdateRolePortIn {

    private final RolePersistencePortOut rolePersistence;

    @Override
    public Role execute(Long id, String newName) {
        // Verify role exists
        rolePersistence.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con id: " + id));

        // Check the new name is not taken by a different role
        rolePersistence.findByName(newName.trim()).ifPresent(existing -> {
            if (!existing.id().equals(id)) {
                throw new IllegalArgumentException("Ya existe un rol con el nombre: " + newName);
            }
        });

        return rolePersistence.save(new Role(id, newName.trim(), null));
    }
}
