package com.portable.microservices.ms_inventory.product.application.usercases;

import com.portable.microservices.ms_inventory.product.domain.model.Product;
import com.portable.microservices.ms_inventory.product.domain.ports.in.FindProductPortIn;
import com.portable.microservices.ms_inventory.product.domain.ports.out.ProductPersistencePortOut;
import com.portable.shared.infrastructure.presentation.PagedResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindProductUseCase implements FindProductPortIn {

    private final ProductPersistencePortOut productPersistence;

    @Override
    public Optional<Product> findById(UUID id) {
        return productPersistence.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productPersistence.findAll();
    }

    @Override
    public long count() {
        return productPersistence.count();
    }

    @Override
    public PagedResponse<Product> findAll(String texto, Long idCategoria, Boolean estado, int page, int size) {
        List<Product> items = productPersistence.findAllWithFilters(texto, idCategoria, estado, page, size);
        long total = productPersistence.countWithFilters(texto, idCategoria, estado);
        return new PagedResponse<>(items, total, page, size);

    }
}
