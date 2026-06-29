package com.portable.microservices.ms_inventory.product.domain.ports.in;

public interface DeleteBrandPortIn {
    void delete(Long id);
}