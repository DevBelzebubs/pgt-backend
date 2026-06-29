package com.portable.microservices.ms_inventory.product.domain.ports.in;

import com.portable.microservices.ms_inventory.product.domain.model.Brand;
import java.util.List;
import java.util.Optional;

public interface FindBrandPortIn {
    Optional<Brand> findById(Long id);
    List<Brand> findAll();
    long count();
}