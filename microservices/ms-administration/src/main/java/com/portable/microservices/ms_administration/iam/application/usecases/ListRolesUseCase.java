package com.portable.microservices.ms_administration.iam.application.usecases;

import java.util.List;

import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.ports.in.ListRolesPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListRolesUseCase implements ListRolesPortIn {

    private final RolePersistencePortOut rolePersistence;

    @Override
    public List<Role> execute() {
        return rolePersistence.findAll();
    }
}
