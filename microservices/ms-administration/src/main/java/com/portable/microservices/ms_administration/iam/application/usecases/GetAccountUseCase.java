package com.portable.microservices.ms_administration.iam.application.usecases;

import java.util.UUID;

import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.ports.in.GetAccountPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAccountUseCase implements GetAccountPortIn {

    private final AccountPersistencePortOut accountPersistence;

    @Override
    public Account execute(UUID uuid) {
        return accountPersistence.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con uuid: " + uuid));
    }
}
