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
        com.portable.microservices.ms_administration.iam.infrastructure.persistence.entity.RoleJpaEntity entity =
                com.portable.microservices.ms_administration.iam.infrastructure.persistence.entity.RoleJpaEntity.builder()
                        .id(role.id())
                        .name(role.name())
                        .build();

        var saved = repository.save(entity);
        return new Role(saved.getId(), saved.getName(), saved.getCreatedAt());
    }

    @Override
    public Optional<Role> findByName(String name) {
        return repository.findByName(name)
                .map(entity -> new Role(entity.getId(), entity.getName(), entity.getCreatedAt()));
    }

    @Override
    public Optional<Role> findById(Long id) {
        return repository.findById(id)
                .map(entity -> new Role(entity.getId(), entity.getName(), entity.getCreatedAt()));
    }

    @Override
    public List<Role> findAll() {
        return repository.findAll().stream()
                .map(e -> new Role(e.getId(), e.getName(), e.getCreatedAt()))
                .toList();
    }
    
    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
