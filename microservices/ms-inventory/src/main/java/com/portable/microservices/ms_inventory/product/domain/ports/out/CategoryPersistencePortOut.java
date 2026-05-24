package com.portable.microservices.ms_inventory.product.domain.ports.out;

import com.portable.microservices.ms_inventory.product.domain.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryPersistencePortOut {
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAll();
    void deleteById(Long id);
    long count();
}