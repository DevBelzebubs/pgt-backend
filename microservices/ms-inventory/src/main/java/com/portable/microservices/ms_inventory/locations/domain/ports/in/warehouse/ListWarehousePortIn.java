package com.portable.microservices.ms_inventory.locations.domain.ports.in.warehouse;

import java.util.List;

import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;

public interface ListWarehousePortIn {
    List<Warehouse> execute();
}
