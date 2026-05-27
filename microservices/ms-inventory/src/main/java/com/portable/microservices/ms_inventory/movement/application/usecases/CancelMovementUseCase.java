package com.portable.microservices.ms_inventory.movement.application.usecases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.movement.domain.ports.in.CancelMovementPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.MovimientoPersistencePortOut;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CancelMovementUseCase implements CancelMovementPortIn {

    private final MovimientoPersistencePortOut persistence;

    @Override
    public void execute(UUID id) {
        persistence.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movimiento no encontrado: " + id));
        persistence.deleteById(id);
    }
}
