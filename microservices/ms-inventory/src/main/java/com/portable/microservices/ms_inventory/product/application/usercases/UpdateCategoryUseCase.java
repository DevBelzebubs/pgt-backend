package com.portable.microservices.ms_inventory.product.application.usercases;

import com.portable.microservices.ms_inventory.product.domain.model.Category;
import com.portable.microservices.ms_inventory.product.domain.ports.in.UpdateCategoryPortIn;
import com.portable.microservices.ms_inventory.product.domain.ports.out.CategoryPersistencePortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCategoryUseCase implements UpdateCategoryPortIn {

    private final CategoryPersistencePortOut categoryPersistence;

    @Override
    @Transactional
    public Category execute(Long id, UpdateCategoryCommand command) {
        Category existing = categoryPersistence.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));

        Category updated = new Category(
            id,
            command.name() != null ? command.name().trim() : existing.categoryName(),
            command.description() != null ? command.description().trim() : existing.description()
        );

        return categoryPersistence.save(updated);
    }
}