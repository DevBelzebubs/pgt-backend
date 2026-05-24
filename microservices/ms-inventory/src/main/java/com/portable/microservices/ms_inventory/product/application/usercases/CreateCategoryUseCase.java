package com.portable.microservices.ms_inventory.product.application.usercases;

import com.portable.microservices.ms_inventory.product.domain.model.Category;
import com.portable.microservices.ms_inventory.product.domain.ports.in.CreateCategoryPortIn;
import com.portable.microservices.ms_inventory.product.domain.ports.out.CategoryPersistencePortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase implements CreateCategoryPortIn {

    private final CategoryPersistencePortOut categoryPersistence;

    @Override
    @Transactional
    public Category execute(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }
        Category category = new Category(null, name.trim(), description != null ? description.trim() : null);
        return categoryPersistence.save(category);
    }
}
