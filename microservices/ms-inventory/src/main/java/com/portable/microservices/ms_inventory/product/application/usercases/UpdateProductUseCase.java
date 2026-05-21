package com.portable.microservices.ms_inventory.product.application.usercases;

import com.portable.microservices.ms_inventory.product.domain.model.Product;
import com.portable.microservices.ms_inventory.product.domain.ports.in.UpdateProductPortIn;
import com.portable.microservices.ms_inventory.product.domain.ports.out.ProductPersistencePortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateProductUseCase implements UpdateProductPortIn {

    private final ProductPersistencePortOut productPersistence;

    @Override
    @Transactional
    public Product execute(UUID id, UpdateProductCommand command) {
        Product existing = productPersistence.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        Product actualizado = new Product(
                existing.id(),
                command.categoryId(),
                command.brandId(),
                command.codProd(),
                command.codAnexo(),
                command.descripcion(),
                command.modelosCompatibles(),
                existing.preCom(),
                existing.preVen(),
                command.estado(),
                existing.fecCreacion()
        );

        return productPersistence.save(actualizado);
    }
}
