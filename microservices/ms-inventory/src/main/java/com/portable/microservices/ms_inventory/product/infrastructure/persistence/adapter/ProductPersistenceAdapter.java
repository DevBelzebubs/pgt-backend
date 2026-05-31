package com.portable.microservices.ms_inventory.product.infrastructure.persistence.adapter;

import com.portable.microservices.ms_inventory.product.domain.model.Product;
import com.portable.microservices.ms_inventory.product.domain.ports.out.ProductPersistencePortOut;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.ProductJpaEntity;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.mapper.ProductMapper;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductPersistencePortOut {

    private final ProductJpaRepository repository;
    private final ProductMapper mapper;

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = mapper.toEntity(product);
        ProductJpaEntity saved = repository.save(entity);
        Integer stockTotal = repository.sumStockByProductId(saved.getId_producto());
        return mapper.toDomain(saved, stockTotal);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return repository.findById(id)
                .map(entity -> {
                    Integer stockTotal = repository.sumStockByProductId(entity.getId_producto());
                    return mapper.toDomain(entity, stockTotal);
                });
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll().stream()
                .map(entity -> {
                    Integer stockTotal = repository.sumStockByProductId(entity.getId_producto());
                    return mapper.toDomain(entity, stockTotal);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public List<Product> findAllWithFilters(String texto, Long idCategoria, Boolean estado, int page, int size) {
        return repository.findAllWithFilters(
                texto, idCategoria, estado, size, page * size).stream().map(entity -> {
                    Integer stockTotal = repository.sumStockByProductId(entity.getId_producto());
                    return mapper.toDomain(entity, stockTotal);
                }).collect(Collectors.toList());

    }

    @Override
    public long countWithFilters(String texto, Long idCategoria, Boolean estado) {
        return repository.countWithFilters(texto, idCategoria, estado);
    }
}
