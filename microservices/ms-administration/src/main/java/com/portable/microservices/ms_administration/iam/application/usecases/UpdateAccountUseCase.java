package com.portable.microservices.ms_administration.iam.application.usecases;

import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.ports.in.UpdateAccountPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateAccountUseCase implements UpdateAccountPortIn {

    private final AccountPersistencePortOut accountPersistence;

    @Override
    public Account execute(UpdateAccountCommand command) {
        Account existing = accountPersistence.findByUuid(command.uuid())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con uuid: " + command.uuid()));

        Account updated = new Account(
                existing.id(),
                existing.uuid(),
                existing.user(),
                existing.userId(),
                command.headquarterId(),
                existing.username(),
                existing.password(),
                command.active(),
                existing.createdAt()
        );

        return accountPersistence.save(updated);
    }
}
