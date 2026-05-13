package com.portable.microservices.ms_administration.iam.application.usecases;

import com.portable.microservices.ms_administration.iam.domain.ports.in.DeleteRolePortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class DeleteRoleUseCase implements DeleteRolePortIn {

    private final RolePersistencePortOut rolePersistence;

    @Override
    @Transactional
    public void execute(Long id) {
        rolePersistence.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con id: " + id));
        rolePersistence.deleteById(id);
    }
}
