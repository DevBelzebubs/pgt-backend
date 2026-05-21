package com.portable.microservices.ms_inventory.product.domain.ports.in;

import com.portable.microservices.ms_inventory.product.domain.model.Product;
import java.math.BigDecimal;

public interface CreateProductPortIn {
    Product execute(CreateProductCommand command);

    record CreateProductCommand(
        Long categoryId,
        Long brandId,
        String codProd,
        String codAnexo,
        String descripcion,
        String modelosCompatibles,
        java.math.BigDecimal preCom,
        java.math.BigDecimal preVen
    ) {}
}
