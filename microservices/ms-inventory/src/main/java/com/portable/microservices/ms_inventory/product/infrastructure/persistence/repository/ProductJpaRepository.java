package com.portable.microservices.ms_inventory.product.infrastructure.persistence.repository;

import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {

    @Query("select case when count(p) > 0 then true else false end from ProductJpaEntity p where p.cod_prod = :codProd")
    boolean existsByCodProd(@Param("codProd") String codProd);
}