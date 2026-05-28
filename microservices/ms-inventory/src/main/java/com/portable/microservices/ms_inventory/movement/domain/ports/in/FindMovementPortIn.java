package com.portable.microservices.ms_inventory.movement.domain.ports.in;

import java.util.Optional;
import java.util.UUID;

import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.MovimientoListadoResponse;

public interface FindMovementPortIn {
    Optional<MovimientoListadoResponse> execute(UUID id);
}
