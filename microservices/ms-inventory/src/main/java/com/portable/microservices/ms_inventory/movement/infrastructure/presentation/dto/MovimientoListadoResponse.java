package com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MovimientoListadoResponse(
    UUID idMovimiento,
    String tipo,
    OffsetDateTime fecha,
    String motivo,
    String documentoRef,
    String nroLote,
    String producto,
    String sku,
    Integer cantidadIngreso,
    Integer cantidadSalida,
    Integer stockActual,
    String locacion,
    String usuario
) {}