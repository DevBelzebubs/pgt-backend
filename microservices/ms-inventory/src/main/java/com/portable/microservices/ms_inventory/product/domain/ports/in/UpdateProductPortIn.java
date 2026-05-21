package com.portable.microservices.ms_inventory.product.domain.ports.in;

import com.portable.microservices.ms_inventory.product.domain.model.Product;
import java.util.UUID;

public interface UpdateProductPortIn {
    Product execute(UUID id, UpdateProductCommand command);

    record UpdateProductCommand(
        Long categoryId,
        Long brandId,
        String codProd,
        String codAnexo,
        String descripcion,
        String modelosCompatibles,
        boolean estado
    ) {}
}
