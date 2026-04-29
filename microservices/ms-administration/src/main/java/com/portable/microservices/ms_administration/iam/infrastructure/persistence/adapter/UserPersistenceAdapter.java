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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Optional<User> findByUuid(UUID uuid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUuid'");
    }

    @Override
    public Optional<User> findByDni(String dni) {
        return repository.findByDni(dni).map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
    
    @Override
    public void deleteById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    
}
