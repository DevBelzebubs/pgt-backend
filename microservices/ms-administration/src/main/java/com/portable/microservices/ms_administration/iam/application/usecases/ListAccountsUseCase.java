package com.portable.microservices.ms_administration.iam.application.usecases;

import java.util.List;

import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.ports.in.ListAccountsPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListAccountsUseCase implements ListAccountsPortIn {

    private final AccountPersistencePortOut accountPersistence;

    @Override
    public List<Account> execute() {
        return accountPersistence.findAll();
    }
}
