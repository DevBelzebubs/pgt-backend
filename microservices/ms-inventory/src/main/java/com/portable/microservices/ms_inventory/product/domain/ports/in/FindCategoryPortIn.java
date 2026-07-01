package com.portable.microservices.ms_inventory.product.domain.ports.in;

import com.portable.microservices.ms_inventory.product.domain.model.Category;
import java.util.List;
import java.util.Optional;

public interface FindCategoryPortIn {
    Optional<Category> findById(Long id);
    List<Category> findAll();
    long count();
}