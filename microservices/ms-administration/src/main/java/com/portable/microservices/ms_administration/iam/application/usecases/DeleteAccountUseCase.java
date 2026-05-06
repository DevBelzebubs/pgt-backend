package com.portable.microservices.ms_administration.iam.application.usecases;

import java.util.UUID;

import com.portable.microservices.ms_administration.iam.domain.ports.in.DeleteAccountPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class DeleteAccountUseCase implements DeleteAccountPortIn {

    private final AccountPersistencePortOut accountPersistence;

    @Override
    @Transactional
    public void execute(UUID uuid) {
        accountPersistence.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con uuid: " + uuid));
        accountPersistence.deleteByUuid(uuid);
    }
}
