package com.portable.microservices.ms_inventory.kardex.application.usecases;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;
import com.portable.microservices.ms_inventory.kardex.domain.ports.in.FindKardexPortIn;
import com.portable.microservices.ms_inventory.kardex.domain.ports.out.KardexPersistencePortOut;
import com.portable.shared.infrastructure.presentation.PagedResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FindKardexUseCase implements FindKardexPortIn {
    private final KardexPersistencePortOut kardexPersistence;
    @Override
    public Optional<Kardex> findLastByProductId(UUID productId) {
        return kardexPersistence.findLastByProductId(productId);
    }
    @Override
    public List<Kardex> findByProductId(UUID productId) {
        return kardexPersistence.findByProductId(productId);
    }
    @Override
    public PagedResponse<Kardex> findByProductId(UUID productId, int page, int size) {
        List<Kardex> items = kardexPersistence.findByProductId(productId, page, size);
        long total = kardexPersistence.countByProductId(productId);
        return new PagedResponse<>(items, total, page, size);
    }
    @Override
    public List<Kardex> findAll() {
        return kardexPersistence.findAll();
    }
    @Override
    public PagedResponse<Kardex> findAll(int page, int size) {
        List<Kardex> items = kardexPersistence.findAll(page, size);
        long total = kardexPersistence.countAllKardex();
        return new PagedResponse<>(items, total, page, size);
    }
}