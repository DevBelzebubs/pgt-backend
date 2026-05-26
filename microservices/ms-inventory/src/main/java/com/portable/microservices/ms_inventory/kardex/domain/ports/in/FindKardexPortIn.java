package com.portable.microservices.ms_inventory.kardex.domain.ports.in;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;

public interface FindKardexPortIn {
    Optional<Kardex> findLastByProductId(UUID productId);

    List<Kardex> findByProductId(UUID productId);

    List<Kardex> findAll();
}
