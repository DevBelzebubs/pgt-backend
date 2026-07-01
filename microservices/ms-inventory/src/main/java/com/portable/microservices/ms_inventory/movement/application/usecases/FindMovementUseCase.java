package com.portable.microservices.ms_inventory.movement.application.usecases;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.movement.domain.ports.in.FindMovementPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.MovimientoPersistencePortOut;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.MovimientoListadoResponse;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.mapper.MovimientoWebMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FindMovementUseCase implements FindMovementPortIn {

    private final MovimientoPersistencePortOut persistence;
    private final MovimientoWebMapper mapper;

    @Override
    public Optional<MovimientoListadoResponse> execute(UUID id) {
        return persistence.findByIdWithDetails(id)
                .map(mapper::toListadoResponse);
    }
}
