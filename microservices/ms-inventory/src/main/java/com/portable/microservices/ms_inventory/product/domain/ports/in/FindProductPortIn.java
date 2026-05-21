package com.portable.microservices.ms_inventory.product.domain.ports.in;

import com.portable.microservices.ms_inventory.product.domain.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindProductPortIn {
    Optional<Product> findById(UUID id);
    List<Product> findAll();
    long count();
}
