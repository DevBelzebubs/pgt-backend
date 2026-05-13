package com.portable.microservices.ms_administration.iam.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;
import com.portable.microservices.ms_administration.iam.infrastructure.persistence.mapper.AccountMapper;
import com.portable.microservices.ms_administration.iam.infrastructure.persistence.repository.AccountJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements AccountPersistencePortOut{
    
    private final AccountJpaRepository repository;
    private final AccountMapper accountMapper;

    @Override
    public Account save(Account account) {
        var entity = accountMapper.toEntity(account);
        var savedEntity = repository.save(entity);

        return accountMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(accountMapper::toDomain);
    }
    @Override
    public List<Account> findAll() {
        return repository.findAll().stream().map(accountMapper::toDomain).toList();
    }
    @Override
    public Optional<Account> findByUuid(java.util.UUID uuid) {
        return repository.findByUuid(uuid).map(accountMapper::toDomain);
    }
    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public void deleteByUuid(java.util.UUID uuid) {
        repository.deleteByUuid(uuid);
    }


}
