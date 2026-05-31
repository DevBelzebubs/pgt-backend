package com.portable.microservices.ms_inventory.kardex.domain.ports.out;

import java.util.Optional;
import java.util.UUID;

import com.portable.microservices.ms_inventory.lot.domain.model.Lot;

public interface LotPersistencePortOut {
    Lot save(Lot lot);
    
    Optional<Lot> findById(UUID id);
}
