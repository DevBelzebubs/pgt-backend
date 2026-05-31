package com.portable.microservices.ms_inventory.shared.infrastructure.websocket.dto;

import java.util.UUID;

public record MovementWsDto(
    UUID id,
    UUID productId,
    String tipo,
    Integer cantidad
) {}
