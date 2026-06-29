package com.portable.microservices.ms_inventory.movement.domain.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.portable.microservices.ms_inventory.movement.domain.model.Movement;

public interface MovementPersistencePortOut {
    Movement save(Movement movement);
    
    Optional<Movement> findById(UUID id);
    
    List<Movement> findAll();
}
