package com.portable.microservices.ms_inventory.locations.presentation.dto;

import java.util.UUID;

public record WarehouseResponse(
    Long id,
    Long idSede,
    UUID almacenUuid,
    String codAlm,
    String nombre,
    String tipo,
    Boolean activo
) {
}