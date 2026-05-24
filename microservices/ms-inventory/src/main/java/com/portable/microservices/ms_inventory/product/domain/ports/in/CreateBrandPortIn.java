package com.portable.microservices.ms_inventory.product.domain.ports.in;

import com.portable.microservices.ms_inventory.product.domain.model.Brand;

public interface CreateBrandPortIn {
    Brand execute(String brandName);
}
