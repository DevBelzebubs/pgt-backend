package com.portable.microservices.ms_inventory.kardex.domain.ports.in;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;
import com.portable.shared.infrastructure.presentation.PagedResponse;

public interface FindKardexPortIn {
    Optional<Kardex> findLastByProductId(UUID productId);

    List<Kardex> findByProductId(UUID productId);

    PagedResponse<Kardex> findByProductId(UUID productId, int page, int size);

    List<Kardex> findAll();

    PagedResponse<Kardex> findAll(int page, int size);
}
