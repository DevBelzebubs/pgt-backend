package com.portable.microservices.ms_inventory.kardex.domain.ports.in;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;
import com.portable.microservices.ms_inventory.kardex.presentation.dto.KardexResponse;
import com.portable.shared.infrastructure.presentation.PagedResponse;

public interface FindKardexPortIn {
    Optional<Kardex> findLastByProductId(UUID productId);

    List<Kardex> findByProductId(UUID id);

    PagedResponse<Kardex> findByProductId(UUID productId, int page, int size);

    List<Kardex> findAll();

    PagedResponse<Kardex> findAll(int page, int size);

    PagedResponse<Kardex> findAllWithFilters(String tipoMovimiento, LocalDate fechaDesde, LocalDate fechaHasta, String texto, int page, int size);
}
