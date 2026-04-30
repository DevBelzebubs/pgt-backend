package com.portable.microservices.ms_administration.iam.application.usecases;

import java.util.UUID;

import com.portable.microservices.ms_administration.iam.domain.ports.in.DeleteUserPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserUseCase implements DeleteUserPortIn {
    private final UserPersistencePortOut userPersistence;

    @Override
    public void execute(UUID uuid) {
        userPersistence.deleteById(uuid.toString());
    }
}
