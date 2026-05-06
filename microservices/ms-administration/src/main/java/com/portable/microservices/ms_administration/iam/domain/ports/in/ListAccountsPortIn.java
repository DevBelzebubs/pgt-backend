package com.portable.microservices.ms_administration.iam.domain.ports.in;

import java.util.List;

import com.portable.microservices.ms_administration.iam.domain.model.Account;

public interface ListAccountsPortIn {
    List<Account> execute();
}
