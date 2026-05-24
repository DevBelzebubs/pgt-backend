package com.portable.microservices.ms_inventory.product.application.usercases;

import com.portable.microservices.ms_inventory.product.domain.model.Brand;
import com.portable.microservices.ms_inventory.product.domain.ports.in.UpdateBrandPortIn;
import com.portable.microservices.ms_inventory.product.domain.ports.out.BrandPersistencePortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateBrandUseCase implements UpdateBrandPortIn {

    private final BrandPersistencePortOut brandPersistence;

    @Override
    @Transactional
    public Brand execute(Long id, UpdateBrandCommand command) {
        Brand existing = brandPersistence.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Marca no encontrada"));

        Brand updated = new Brand(
            id,
            command.name() != null ? command.name().trim() : existing.brandName()
        );

        return brandPersistence.save(updated);
    }
}