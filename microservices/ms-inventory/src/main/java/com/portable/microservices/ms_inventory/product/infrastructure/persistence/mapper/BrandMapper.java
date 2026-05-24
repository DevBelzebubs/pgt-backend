package com.portable.microservices.ms_inventory.product.infrastructure.persistence.mapper;

import com.portable.microservices.ms_inventory.product.domain.model.Brand;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.BrandJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public BrandJpaEntity toEntity(Brand brand) {
        if (brand == null) {
            return null;
        }

        return BrandJpaEntity.builder()
                .id(brand.id())
                .brandName(brand.brandName())
                .build();
    }

    public Brand toDomain(BrandJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Brand(
                entity.getId(),
                entity.getBrandName()
        );
    }

    public BrandJpaEntity toEntityById(Long id) {
        if (id == null) {
            return null;
        }

        return BrandJpaEntity.builder()
                .id(id)
                .build();
    }
}