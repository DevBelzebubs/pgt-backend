package com.portable.microservices.ms_inventory.locations.application.usecases.warehouse;

import java.util.List;

import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.warehouse.ListWarehousePortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.out.WarehousePersistencePortOut;

public class ListWarehoureUseCase implements ListWarehousePortIn{

    private final WarehousePersistencePortOut persistence;

    public ListWarehoureUseCase(WarehousePersistencePortOut persistence) {
        this.persistence = persistence;
    }

    @Override
    public List<Warehouse> execute() {
        return persistence.findAllActives();
    }


    

}
