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
                command.id_categoria() != null ? command.id_categoria() : existing.categoryId(),
                command.id_marca() != null ? command.id_marca() : existing.brandId(),
                command.cod_prod() != null ? command.cod_prod() : existing.codProd(),
                command.cod_anexo() != null ? command.cod_anexo() : existing.codAnexo(),
                command.descripcion() != null ? command.descripcion() : existing.descripcion(),
                command.modelosCompatibles() != null ? command.modelosCompatibles() : existing.modelosCompatibles(),
                existing.preCom(),
                existing.preVen(),
                command.estado() != null ? command.estado() : existing.estado(),
                existing.fecCreacion(),
                existing.stockMinimo(),
                existing.stockTotal()
        );

        return productPersistence.save(actualizado);
    }
}
