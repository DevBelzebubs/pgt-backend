package com.portable.microservices.ms_inventory.product.application.usercases;

import com.portable.microservices.ms_inventory.product.domain.ports.in.DeleteBrandPortIn;
import com.portable.microservices.ms_inventory.product.domain.ports.out.BrandPersistencePortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBrandUseCase implements DeleteBrandPortIn {

    private final BrandPersistencePortOut brandPersistence;

    @Override
    public void delete(Long id) {
        brandPersistence.deleteById(id);
    }
}