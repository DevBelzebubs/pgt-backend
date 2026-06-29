package com.portable.microservices.ms_administration.iam.application.usecases;

import java.util.UUID;

import com.portable.microservices.ms_administration.iam.domain.model.User;
import com.portable.microservices.ms_administration.iam.domain.ports.in.GetUserPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetUserUseCase implements GetUserPortIn {
    private final UserPersistencePortOut userPersistence;

    @Override
    public User execute(UUID uuid) {
        return userPersistence.findByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}
