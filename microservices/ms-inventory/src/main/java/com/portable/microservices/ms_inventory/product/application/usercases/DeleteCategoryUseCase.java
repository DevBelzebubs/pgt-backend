package com.portable.microservices.ms_inventory.product.application.usercases;

import com.portable.microservices.ms_inventory.product.domain.ports.in.DeleteCategoryPortIn;
import com.portable.microservices.ms_inventory.product.domain.ports.out.CategoryPersistencePortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase implements DeleteCategoryPortIn {

    private final CategoryPersistencePortOut categoryPersistence;

    @Override
    public void delete(Long id) {
        categoryPersistence.deleteById(id);
    }
}