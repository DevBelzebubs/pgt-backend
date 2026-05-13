package com.portable.microservices.ms_administration.iam.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_administration.iam.domain.model.User;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;
import com.portable.microservices.ms_administration.iam.infrastructure.persistence.mapper.UserMapper;
import com.portable.microservices.ms_administration.iam.infrastructure.persistence.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePortOut {
    private final UserJpaRepository repository;
    private final UserMapper mapper;

    @Override
    public User save(User user) {
        var entity = mapper.toEntity(user);
        var savedEntity = repository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(String id) {
        try {
            Long longId = Long.valueOf(id);
            return repository.findById(longId).map(mapper::toDomain);
        } catch (NumberFormatException e) {
            try {
                UUID uuid = UUID.fromString(id);
                return repository.findByUuid(uuid).map(mapper::toDomain);
            } catch (IllegalArgumentException ex) {
                return Optional.empty();
            }
        }
    }

    @Override
    public Optional<User> findByUuid(UUID uuid) {
        return repository.findByUuid(uuid).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByDni(String dni) {
        return repository.findByDni(dni).map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }
    
    @Override
    public void deleteById(String id) {
        try {
            Long longId = Long.valueOf(id);
            repository.deleteById(longId);
        } catch (NumberFormatException e) {
            try {
                UUID uuid = UUID.fromString(id);
                repository.deleteByUuid(uuid);
            } catch (IllegalArgumentException ex) {
                // invalid id format — no-op
            }
        }
    }

    
}
