package com.portable.microservices.ms_inventory.locations.domain.ports.in.warehouse;

public interface DeleteWarehousePortIn {
    void execute(Long id);
}
