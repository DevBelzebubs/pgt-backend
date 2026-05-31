package com.portable.microservices.ms_inventory.kardex.domain.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;

public interface KardexPersistencePortOut {
    Kardex save(Kardex kardex);

    Optional<Kardex> findById(UUID id);

    Optional<Kardex> findLastByProductId(UUID productId);

    List<Kardex> findByProductId(UUID productId);

    List<Kardex> findByProductId(UUID productId, int page, int size);

    long countByProductId(UUID productId);

    List<Kardex> findAll();

    List<Kardex> findAll(int page, int size);

    long countAllKardex();
}
