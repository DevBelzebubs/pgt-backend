package com.portable.microservices.ms_administration.iam.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;
import com.portable.microservices.ms_administration.iam.infrastructure.persistence.repository.RoleJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RolePersistenceAdapter implements RolePersistencePortOut {
    
    private final RoleJpaRepository repository;

    @Override
    public Role save(Role role) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public Optional<Role> findByName(String name) {
        return repository.findByName(name)
                .map(entity -> new Role(entity.getId(), entity.getName(), entity.getCreatedAt()));
    }

    @Override
    public List<Role> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
}
