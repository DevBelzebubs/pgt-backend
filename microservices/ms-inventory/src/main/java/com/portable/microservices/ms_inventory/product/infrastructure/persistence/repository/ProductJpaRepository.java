package com.portable.microservices.ms_inventory.product.infrastructure.persistence.repository;

import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {
    boolean existsByCodProd(String codProd);
}
