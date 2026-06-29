package com.portable.microservices.ms_administration.iam.application.usecases;

import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.ports.in.GetRolePortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetRoleUseCase implements GetRolePortIn {

    private final RolePersistencePortOut rolePersistence;

    @Override
    public Role execute(Long id) {
        return rolePersistence.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con id: " + id));
    }
}
