package com.portable.microservices.ms_inventory.product.domain.ports.in;

import com.portable.microservices.ms_inventory.product.domain.model.Category;

public interface UpdateCategoryPortIn {
    Category execute(Long id, UpdateCategoryCommand command);

    record UpdateCategoryCommand(
        String name,
        String description
    ) {}
}