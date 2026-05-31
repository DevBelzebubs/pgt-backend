package com.portable.microservices.ms_inventory.movement.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Movement(
    UUID id,
    UUID lotId,
    Long userId,
    TipoMovimiento tipo,
    Integer cantidad,
    OffsetDateTime fecha,
    String motivo,
    String docRef
) {}