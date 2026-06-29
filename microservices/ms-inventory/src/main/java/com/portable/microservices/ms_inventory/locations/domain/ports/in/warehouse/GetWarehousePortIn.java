package com.portable.microservices.ms_inventory.locations.domain.ports.in.warehouse;

import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;

public interface GetWarehousePortIn {
    Warehouse execute(Long id);
}
