package com.portable.microservices.ms_inventory.product.domain.ports.in;

public interface DeleteCategoryPortIn {
    void delete(Long id);
}