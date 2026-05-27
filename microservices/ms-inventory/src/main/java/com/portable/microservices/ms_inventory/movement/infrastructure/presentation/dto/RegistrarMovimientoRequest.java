package com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegistrarMovimientoRequest(
    @NotBlank String tipo,
    UUID idProducto,
    UUID idLote,
    String idLocacion,
    @NotNull @Positive Integer cantidad,
    @NotBlank String motivo,
    String documentoRef,
    String proveedor,
    String nroLote,
    BigDecimal costoUnit,
    LocalDate fecGarantia
) {}