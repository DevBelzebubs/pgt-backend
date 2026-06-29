package com.portable.microservices.ms_inventory.product.application.usercases;

import com.portable.microservices.ms_inventory.product.domain.model.Brand;
import com.portable.microservices.ms_inventory.product.domain.ports.in.CreateBrandPortIn;
import com.portable.microservices.ms_inventory.product.domain.ports.out.BrandPersistencePortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateBrandUseCase implements CreateBrandPortIn {

    private final BrandPersistencePortOut brandPersistence;

    @Override
    @Transactional
    public Brand execute(String brandName) {
        if (brandName == null || brandName.isBlank()) {
            throw new IllegalArgumentException("El nombre de la marca no puede estar vacío");
        }
        Brand brand = new Brand(null, brandName.trim());
        return brandPersistence.save(brand);
    }
}
