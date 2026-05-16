package com.portable.microservices.ms_inventory.locations.application.usecases.warehouse;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.warehouse.UpdateWarehousePortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.out.WarehousePersistencePortOut;

@Service
public class UpdateWarehouseUseCase implements UpdateWarehousePortIn{

    private final WarehousePersistencePortOut persistence;

    public UpdateWarehouseUseCase(WarehousePersistencePortOut persistence) {
        this.persistence = persistence;
    }

@Override
    public Warehouse execute(Long id, Warehouse warehouse) {
        Warehouse existing = persistence.findById(id)
            .orElseThrow(() -> new IllegalStateException("Almacén no encontrado"));

        if (existing.activo() == false) {
            throw new IllegalStateException("El almacén está inactivo y no se puede actualizar");
        }

        Warehouse updated = existing.toBuilder()
            // Si el nuevo codAlm no es nulo ni está vacío, úsalo. Si no, quédate con el existente.
            .codAlm(warehouse.codAlm() != null && !warehouse.codAlm().isBlank() ? warehouse.codAlm() : existing.codAlm())
            
            // Lo mismo para el nombre
            .nombre(warehouse.nombre() != null && !warehouse.nombre().isBlank() ? warehouse.nombre() : existing.nombre())
            
            // Lo mismo para el tipo
            .tipo(warehouse.tipo() != null && !warehouse.tipo().isBlank() ? warehouse.tipo() : existing.tipo())
            
            .build();

        return persistence.save(updated);
    }

}
