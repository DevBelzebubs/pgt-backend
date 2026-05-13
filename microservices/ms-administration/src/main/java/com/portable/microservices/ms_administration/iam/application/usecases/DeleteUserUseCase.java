package com.portable.microservices.ms_administration.iam.application.usecases;

import java.util.UUID;

import com.portable.microservices.ms_administration.iam.domain.ports.in.DeleteUserPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class DeleteUserUseCase implements DeleteUserPortIn {

    private final UserPersistencePortOut userPersistence;
    private final AccountPersistencePortOut accountPersistence;

    @Override
    @Transactional
    public void execute(UUID uuid) {
        var user = userPersistence.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con uuid: " + uuid));

        // Delete associated account first (FK constraint)
        accountPersistence.findAll().stream()
                .filter(a -> a.userId() != null && a.userId().equals(user.id()))
                .forEach(a -> accountPersistence.deleteByUuid(a.uuid()));

        userPersistence.deleteById(String.valueOf(user.id()));
    }
}
