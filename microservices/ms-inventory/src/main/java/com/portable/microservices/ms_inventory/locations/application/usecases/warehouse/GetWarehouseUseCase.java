package com.portable.microservices.ms_inventory.locations.application.usecases.warehouse;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.warehouse.GetWarehousePortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.out.WarehousePersistencePortOut;

@Service
public class GetWarehouseUseCase implements GetWarehousePortIn {

    private final WarehousePersistencePortOut persistence;

    public GetWarehouseUseCase(WarehousePersistencePortOut persistence) {
        this.persistence = persistence;
    }

    @Override
    public Warehouse execute(Long id) {
        Warehouse warehouse = persistence.findById(id)
            .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));
        if (!warehouse.activo()) {
            throw new RuntimeException("Almacén no activo o no encontrado");
        }
        return warehouse;
    }

    

}
