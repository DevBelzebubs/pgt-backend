package com.portable.microservices.ms_inventory.product.presentation.mapper;

import com.portable.microservices.ms_inventory.product.domain.model.Product;
import com.portable.microservices.ms_inventory.product.presentation.dto.CreateProductRequest;
import com.portable.microservices.ms_inventory.product.presentation.dto.ProductResponse;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.UUID;

@Component
public class ProductPresentationMapper {

    public Product toDomain(CreateProductRequest request) {
        return new Product(
                UUID.randomUUID(),
                request.categoryId(),
                request.brandId(),
                request.codProd(),
                request.codAnexo(),
                request.descripcion(),
                request.modelosCompatibles(),
                request.preCom(),
                request.preVen(),
                true,
                ZonedDateTime.now()
        );
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.id(),
                product.categoryId(),
                product.brandId(),
                product.codProd(),
                product.codAnexo(),
                product.descripcion(),
                product.modelosCompatibles(),
                product.preCom(),
                product.preVen(),
                product.estado(),
                product.fecCreacion()
        );
    }
}
