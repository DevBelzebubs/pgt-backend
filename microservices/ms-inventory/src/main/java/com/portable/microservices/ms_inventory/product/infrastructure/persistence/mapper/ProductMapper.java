package com.portable.microservices.ms_inventory.product.infrastructure.persistence.mapper;

import com.portable.microservices.ms_inventory.product.domain.model.Product;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductJpaEntity toEntity(Product product) {
        if (product == null) return null;

        return ProductJpaEntity.builder()
                .id(product.id())
                .categoryId(product.categoryId())
                .brandId(product.brandId())
                .codProd(product.codProd())
                .codAnexo(product.codAnexo())
                .descripcion(product.descripcion())
                .modelosCompatibles(product.modelosCompatibles())
                .preCom(product.preCom())
                .preVen(product.preVen())
                .estado(product.estado())
                .fecCreacion(product.fecCreacion())
                .build();
    }

    public Product toDomain(ProductJpaEntity entity) {
        if (entity == null) return null;

        return new Product(
                entity.getId(),
                entity.getCategoryId(),
                entity.getBrandId(),
                entity.getCodProd(),
                entity.getCodAnexo(),
                entity.getDescripcion(),
                entity.getModelosCompatibles(),
                entity.getPreCom(),
                entity.getPreVen(),
                entity.isEstado(),
                entity.getFecCreacion()
        );
    }
}
