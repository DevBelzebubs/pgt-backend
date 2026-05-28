package com.portable.microservices.ms_inventory.product.infrastructure.persistence.repository;

import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM ProductJpaEntity p WHERE p.cod_prod = :codProd")
    boolean existsByCodProd(@Param("codProd") String codProd);
    @Query(value = """
        SELECT COALESCE(SUM(l.cantidad), 0)
        FROM inventory.lote l
        WHERE l.id_producto = :productId
    """, nativeQuery = true)
    Integer sumStockByProductId(@Param("productId") UUID productId);
}
