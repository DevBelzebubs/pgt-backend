package com.portable.microservices.ms_inventory.product.infrastructure.persistence.repository;

import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @Query(value = """
                SELECT p.* FROM inventory.producto p
                WHERE (:texto IS NULL OR :texto = ''
                       OR LOWER(p.cod_prod) LIKE LOWER(CONCAT('%', :texto, '%'))
                       OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')))
                AND (:idCategoria IS NULL OR p.id_categoria = :idCategoria)
                AND (:estado IS NULL OR p.estado = :estado)
                ORDER BY p.fec_creacion DESC
                LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<ProductJpaEntity> findAllWithFilters(
            @Param("texto") String texto,
            @Param("idCategoria") Long idCategoria,
            @Param("estado") Boolean estado,
            @Param("limit") int limit,
            @Param("offset") int offset);

    @Query(value = """
                SELECT COUNT(*) FROM inventory.producto p
                WHERE (:texto IS NULL OR :texto = ''
                       OR LOWER(p.cod_prod) LIKE LOWER(CONCAT('%', :texto, '%'))
                       OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')))
                AND (:idCategoria IS NULL OR p.id_categoria = :idCategoria)
                AND (:estado IS NULL OR p.estado = :estado)
            """, nativeQuery = true)
    long countWithFilters(
            @Param("texto") String texto,
            @Param("idCategoria") Long idCategoria,
            @Param("estado") Boolean estado);
}
