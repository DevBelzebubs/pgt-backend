package com.portable.microservices.ms_inventory.movement.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.portable.microservices.ms_inventory.movement.domain.model.Movement;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.MovementPersistencePortOut;
import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.mapper.MovementMapper;
import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.repository.MovementJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovementPersistenceAdapter implements MovementPersistencePortOut {
    private final MovementJpaRepository repository;
    private final MovementMapper mapper;
    @Override
    public Movement save(Movement movement) {
        return mapper.toDomain(repository.save(mapper.toEntity(movement)));
    }
    @Override
    public Optional<Movement> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }
    @Override
    public List<Movement> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }
}