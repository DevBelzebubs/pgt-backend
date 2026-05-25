package com.portable.microservices.ms_inventory.product.infrastructure.persistence.mapper;

import com.portable.microservices.ms_inventory.product.domain.model.Category;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.CategoryJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryJpaEntity toEntity(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryJpaEntity.builder()
                .id(category.id())
                .categoryName(category.categoryName())
                .description(category.description())
                .build();
    }

    public Category toDomain(CategoryJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Category(
                entity.getId(),
                entity.getCategoryName(),
                entity.getDescription()
        );
    }

    public CategoryJpaEntity toEntityById(Long id) {
        if (id == null) {
            return null;
        }

        return CategoryJpaEntity.builder()
                .id(id)
                .build();
    }
}