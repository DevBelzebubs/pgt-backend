package com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.adapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public List<Kardex> findByProductId(UUID productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByProductoIdPaged(productId, pageable).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }
    @Override
    public long countByProductId(UUID productId) {
        return repository.countByProductoId(productId);
    }
    @Override
    public List<Kardex> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }
    @Override
    public List<Kardex> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllPaged(pageable).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }
    @Override
    public long countAllKardex() {
        return repository.countAllKardex();
    }

    @Override
    public List<Kardex> findAllWithFilters(String tipoMovimiento, LocalDate fechaDesde, LocalDate fechaHasta, String texto, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllWithFilters(tipoMovimiento, fechaDesde, fechaHasta, texto, pageable).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public long countAllWithFilters(String tipoMovimiento, LocalDate fechaDesde, LocalDate fechaHasta, String texto) {
        return repository.countAllWithFilters(tipoMovimiento, fechaDesde, fechaHasta, texto);
    }
}