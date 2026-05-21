package com.portable.microservices.ms_inventory.product.application.usercases;
import com.portable.microservices.ms_inventory.product.domain.model.Product;
import com.portable.microservices.ms_inventory.product.domain.ports.in.CreateProductPortIn;
import com.portable.microservices.ms_inventory.product.domain.ports.out.ProductPersistencePortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateProductUseCase implements CreateProductPortIn {

    private final ProductPersistencePortOut productPersistence;

    @Override
    @Transactional
    public Product execute(CreateProductCommand command) {
        if (productPersistence.findAll().stream()
                .anyMatch(p -> p.codProd().equals(command.codProd()))) {
            throw new IllegalArgumentException("Ya existe un producto con el código: " + command.codProd());
        }

        Product nuevo = new Product(
            UUID.randomUUID(),
            command.categoryId(),
            command.brandId(),
            command.codProd(),
            command.codAnexo(),
            command.descripcion(),
            command.modelosCompatibles(),
            command.preCom(),
            command.preVen(),
            true,
            ZonedDateTime.now()
        );

        return productPersistence.save(nuevo);
    }
}