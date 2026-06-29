package com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MovimientoResponse(
    UUID idMovimiento,
    UUID idLote,
    Long idUsuario,
    String tipo,
    Integer cantidad,
    OffsetDateTime fecha,
    String motivo,
    String docRef
) {
}
