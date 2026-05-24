package com.portable.microservices.ms_inventory.product.domain.ports.out;

import com.portable.microservices.ms_inventory.product.domain.model.Brand;
import java.util.List;
import java.util.Optional;

public interface BrandPersistencePortOut {
    Brand save(Brand brand);
    Optional<Brand> findById(Long id);
    List<Brand> findAll();
    void deleteById(Long id);
    long count();
}