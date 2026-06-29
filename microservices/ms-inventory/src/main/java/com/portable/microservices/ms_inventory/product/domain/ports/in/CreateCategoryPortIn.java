package com.portable.microservices.ms_inventory.product.domain.ports.in;

import com.portable.microservices.ms_inventory.product.domain.model.Category;

public interface CreateCategoryPortIn {
    Category execute(String name, String description);
}
