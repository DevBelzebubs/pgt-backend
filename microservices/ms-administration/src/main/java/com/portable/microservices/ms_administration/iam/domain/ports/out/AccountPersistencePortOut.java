package com.portable.microservices.ms_administration.iam.domain.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.portable.microservices.ms_administration.iam.domain.model.Account;

public interface AccountPersistencePortOut {
    Account save(Account account);
    Optional<Account> findByUsername(String username);
    Optional<Account> findByUuid(UUID uuid);
    List<Account> findAll();
    boolean existsByUsername(String username);
    void deleteByUuid(UUID uuid);
}
