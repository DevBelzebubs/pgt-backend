package com.portable.microservices.ms_inventory.locations.application.usecases.warehouse;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.warehouse.DeleteWarehousePortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.out.WarehousePersistencePortOut;

@Service
public class DeleteWarehouseUseCase implements DeleteWarehousePortIn {

    private final WarehousePersistencePortOut persistence;

    public DeleteWarehouseUseCase(WarehousePersistencePortOut persistence) {
        this.persistence = persistence;
    }

    @Override
    public void execute(Long id) {
        Warehouse existing = persistence.findById(id)
            .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

        Warehouse disabled = existing.toBuilder()
            .activo(false)
            .build();

        persistence.save(disabled);
    }

}
