package com.portable.microservices.ms_inventory.product.domain.ports.out;

import com.portable.microservices.ms_inventory.product.domain.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductPersistencePortOut {
    Product save(Product product);
    Optional<Product> findById(UUID id);
    List<Product> findAll();
    void deleteById(UUID id);
    long count();
}
