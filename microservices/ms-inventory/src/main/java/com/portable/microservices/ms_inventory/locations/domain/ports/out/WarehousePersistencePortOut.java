package com.portable.microservices.ms_inventory.locations.domain.ports.out;

import java.util.List;
import java.util.Optional;

import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;

public interface WarehousePersistencePortOut {
    Warehouse save(Warehouse warehouse);
    Optional<Warehouse> findById(Long id);
    boolean existsByCodAlm(String codAlm);
    boolean existsById(Long id); // Crucial para validar cuando creemos la locación

    List<Warehouse> findAllActives();
}
