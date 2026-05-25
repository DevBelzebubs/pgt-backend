package com.portable.microservices.ms_inventory.movement.domain.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.portable.microservices.ms_inventory.movement.domain.model.Movimiento;

public interface MovimientoPersistencePortOut {
    Movimiento save(Movimiento movimiento);
    
    Optional<Movimiento> findById(UUID id);
    
    List<Movimiento> findByIdLote(UUID idLote);
}
