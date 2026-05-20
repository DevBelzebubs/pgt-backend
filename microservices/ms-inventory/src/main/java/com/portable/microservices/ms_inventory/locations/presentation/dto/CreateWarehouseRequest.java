package com.portable.microservices.ms_inventory.locations.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateWarehouseRequest(
    @NotNull(message = "El ID de sede es obligatorio")
    Long idSede,

    @NotBlank(message = "El código de almacén es obligatorio")
    String codAlm,

    @NotBlank(message = "El nombre del almacén es obligatorio")
    String nombre,

    String tipo
) {
}