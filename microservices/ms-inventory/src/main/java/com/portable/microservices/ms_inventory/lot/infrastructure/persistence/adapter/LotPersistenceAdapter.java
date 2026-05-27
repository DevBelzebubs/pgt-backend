package com.portable.microservices.ms_inventory.lot.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.kardex.domain.ports.out.LotPersistencePortOut;
import com.portable.microservices.ms_inventory.lot.domain.model.Lot;
import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.mapper.LotMapper;
import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.repository.LotJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LotPersistenceAdapter implements LotPersistencePortOut {
    private final LotJpaRepository repository;
    private final LotMapper mapper;
    @Override
    public Lot save(Lot lot) {
        return mapper.toDomain(repository.save(mapper.toEntity(lot)));
    }
    @Override
    public Optional<Lot> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}