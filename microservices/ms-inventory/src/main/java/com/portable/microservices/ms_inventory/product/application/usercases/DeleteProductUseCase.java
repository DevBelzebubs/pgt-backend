package com.portable.microservices.ms_inventory.product.application.usercases;

import com.portable.microservices.ms_inventory.product.domain.ports.in.DeleteProductPortIn;
import com.portable.microservices.ms_inventory.product.domain.ports.out.ProductPersistencePortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteProductUseCase implements DeleteProductPortIn {

    private final ProductPersistencePortOut productPersistence;

    @Override
    public void delete(UUID id) {
        productPersistence.deleteById(id);
    }
}
