package com.portable.microservices.ms_inventory.product.infrastructure.persistence.adapter;

import com.portable.microservices.ms_inventory.product.domain.model.Brand;
import com.portable.microservices.ms_inventory.product.domain.ports.out.BrandPersistencePortOut;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.BrandJpaEntity;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.mapper.BrandMapper;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.repository.BrandJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BrandPersistenceAdapter implements BrandPersistencePortOut {

    private final BrandJpaRepository repository;
    private final BrandMapper mapper;

    @Override
    public Brand save(Brand brand) {
        BrandJpaEntity entity = mapper.toEntity(brand);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Brand> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }
}