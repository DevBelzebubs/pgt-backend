package com.portable.microservices.ms_inventory.product.infrastructure.persistence.repository;

import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.BrandJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandJpaRepository extends JpaRepository<BrandJpaEntity, Long> {
}
