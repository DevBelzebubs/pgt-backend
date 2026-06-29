package com.portable.microservices.ms_inventory.product.application.usercases;

import com.portable.microservices.ms_inventory.product.domain.model.Brand;
import com.portable.microservices.ms_inventory.product.domain.ports.in.FindBrandPortIn;
import com.portable.microservices.ms_inventory.product.domain.ports.out.BrandPersistencePortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindBrandUseCase implements FindBrandPortIn {

    private final BrandPersistencePortOut brandPersistence;

    @Override
    public Optional<Brand> findById(Long id) {
        return brandPersistence.findById(id);
    }

    @Override
    public List<Brand> findAll() {
        return brandPersistence.findAll();
    }

    @Override
    public long count() {
        return brandPersistence.count();
    }
}