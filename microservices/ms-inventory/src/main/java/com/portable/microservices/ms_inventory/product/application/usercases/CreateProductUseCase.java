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
                .anyMatch(p -> p.codProd().equals(command.cod_prod()))) {
            throw new IllegalArgumentException("Ya existe un producto con el código: " + command.cod_prod());
        }

        Product nuevo = new Product(
            UUID.randomUUID(),
            command.id_categoria(),
            command.id_marca(),
            command.cod_prod(),
            command.cod_anexo(),
            command.descripcion(),
            command.modelos_compatibles(),
            command.pre_com(),
            command.pre_ven(),
            true,
            ZonedDateTime.now()
        );

        return productPersistence.save(nuevo);
    }
}