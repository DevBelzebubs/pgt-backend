package com.portable.microservices.ms_inventory.locations.application.usecases.location;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.locations.domain.model.Location;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.location.CreateLocationPortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.out.LocationPersistencePortOut;
import com.portable.microservices.ms_inventory.locations.domain.ports.out.WarehousePersistencePortOut;

@Service
public class CreateLocationUseCase implements CreateLocationPortIn {

    private final LocationPersistencePortOut persistencyLocation;
    private final WarehousePersistencePortOut persistencyWarehouse;

    public CreateLocationUseCase(LocationPersistencePortOut locationPortOut, WarehousePersistencePortOut warehousePortOut) {
        this.persistencyLocation = locationPortOut;
        this.persistencyWarehouse = warehousePortOut;
    }

    @Override
    public Location execute(Location location) {
        if (location == null || !location.isValidForCreation()) {
            throw new IllegalArgumentException("Location data inválida para creación");
        }

        if (!persistencyWarehouse.existsById(location.idAlmacen())) {
            throw new IllegalStateException("Almacén no existe: " + location.idAlmacen());
        }

        if (location.codBarras() != null && persistencyLocation.existsByCodBarras(location.codBarras())) {
            throw new IllegalStateException("Ya existe una locación con ese código de barras");
        }

        return persistencyLocation.save(location);
    }

}
