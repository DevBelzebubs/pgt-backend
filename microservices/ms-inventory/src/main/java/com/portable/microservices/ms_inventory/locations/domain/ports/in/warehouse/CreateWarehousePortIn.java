package com.portable.microservices.ms_inventory.locations.domain.ports.in.warehouse;

import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;

public interface CreateWarehousePortIn {
    Warehouse execute(Warehouse warehouse);
}
