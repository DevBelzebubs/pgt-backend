package com.portable.microservices.ms_inventory.product.infrastructure.persistence.adapter;

import com.portable.microservices.ms_inventory.product.domain.model.Category;
import com.portable.microservices.ms_inventory.product.domain.ports.out.CategoryPersistencePortOut;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.CategoryJpaEntity;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.mapper.CategoryMapper;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryPersistencePortOut {

    private final CategoryJpaRepository repository;
    private final CategoryMapper mapper;

    @Override
    public Category save(Category category) {
        CategoryJpaEntity entity = mapper.toEntity(category);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Category> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
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