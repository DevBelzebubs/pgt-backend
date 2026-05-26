package com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;
import com.portable.microservices.ms_inventory.kardex.domain.ports.out.KardexPersistencePortOut;
import com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.mapper.KardexMapper;
import com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.repository.KardexJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KardexPersistenceAdapter implements KardexPersistencePortOut {
    private final KardexJpaRepository repository;
    private final KardexMapper mapper;
    @Override
    public Kardex save(Kardex kardex) {
        return mapper.toDomain(repository.save(mapper.toEntity(kardex)));
    }
    @Override
    public Optional<Kardex> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }
    @Override
    public Optional<Kardex> findLastByProductId(UUID productId) {
        return repository.findTopByProductoIdOrderByIdKardexDesc(productId).map(mapper::toDomain);
    }
    @Override
    public List<Kardex> findByProductId(UUID productId) {
        return repository.findByProductoIdOrderByIdKardexAsc(productId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }
    @Override
    public List<Kardex> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }
}