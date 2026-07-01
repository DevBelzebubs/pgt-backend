package com.portable.microservices.ms_inventory.product.presentation.mapper;

import com.portable.microservices.ms_inventory.product.domain.model.Category;
import com.portable.microservices.ms_inventory.product.presentation.dto.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryPresentationMapper {

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.id(), category.categoryName(), category.description());
    }
}
