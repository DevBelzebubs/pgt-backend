package com.portable.microservices.ms_inventory.product.application.usercases;

import com.portable.microservices.ms_inventory.product.domain.model.Category;
import com.portable.microservices.ms_inventory.product.domain.ports.in.FindCategoryPortIn;
import com.portable.microservices.ms_inventory.product.domain.ports.out.CategoryPersistencePortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindCategoryUseCase implements FindCategoryPortIn {

    private final CategoryPersistencePortOut categoryPersistence;

    @Override
    public Optional<Category> findById(Long id) {
        return categoryPersistence.findById(id);
    }

    @Override
    public List<Category> findAll() {
        return categoryPersistence.findAll();
    }

    @Override
    public long count() {
        return categoryPersistence.count();
    }
}