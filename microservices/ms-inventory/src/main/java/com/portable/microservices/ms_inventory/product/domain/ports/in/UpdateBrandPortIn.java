package com.portable.microservices.ms_inventory.product.domain.ports.in;

import com.portable.microservices.ms_inventory.product.domain.model.Brand;

public interface UpdateBrandPortIn {
    Brand execute(Long id, UpdateBrandCommand command);

    record UpdateBrandCommand(
        String name
    ) {}
}