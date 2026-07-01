package com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.entity.KardexJpaEntity;

@Repository
public interface KardexJpaRepository extends JpaRepository<KardexJpaEntity, UUID> {

    @Query("SELECT k FROM KardexJpaEntity k WHERE k.producto.id_producto = :idProducto ORDER BY k.movimiento.fecha DESC, k.idKardex DESC")
    List<KardexJpaEntity> findLatestByProducto(@Param("idProducto") UUID idProducto, Pageable pageable);

    @Query("SELECT k FROM KardexJpaEntity k WHERE k.producto.id_producto = :productId ORDER BY k.movimiento.fecha DESC, k.idKardex DESC")
    List<KardexJpaEntity> findByProductoIdOrderByIdKardexDesc(@Param("productId") UUID productId);

    default Optional<KardexJpaEntity> findTopByProductoIdOrderByIdKardexDesc(UUID productId) {
        List<KardexJpaEntity> lista = findByProductoIdOrderByIdKardexDesc(productId);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Query("SELECT k FROM KardexJpaEntity k WHERE k.producto.id_producto = :productId ORDER BY k.movimiento.fecha ASC, k.idKardex ASC")
    List<KardexJpaEntity> findByProductoIdOrderByIdKardexAsc(@Param("productId") UUID productId);

    @Query("SELECT k FROM KardexJpaEntity k WHERE k.producto.id_producto = :productId ORDER BY k.movimiento.fecha DESC, k.idKardex DESC")
    List<KardexJpaEntity> findByProductoIdPaged(@Param("productId") UUID productId, Pageable pageable);

    @Query("SELECT COUNT(k) FROM KardexJpaEntity k WHERE k.producto.id_producto = :productId")
    long countByProductoId(@Param("productId") UUID productId);

    @Query("SELECT k FROM KardexJpaEntity k ORDER BY k.movimiento.fecha DESC, k.idKardex DESC")
    List<KardexJpaEntity> findAllPaged(Pageable pageable);

    @Query("""
                SELECT k FROM KardexJpaEntity k
                WHERE (:tipoMovimiento IS NULL OR :tipoMovimiento = '' OR k.movimiento.tipo = :tipoMovimiento
                       OR (:tipoMovimiento = 'AJUSTE' AND k.movimiento.tipo IN ('AJUSTE_POSITIVO', 'AJUSTE_NEGATIVO')))
                  AND (:fechaDesde IS NULL OR k.movimiento.fecha >= :fechaDesde)
                  AND (:fechaHasta IS NULL OR k.movimiento.fecha <= :fechaHasta)
                  AND (:texto IS NULL OR :texto = '' OR
                       LOWER(k.producto.cod_prod) LIKE LOWER(CONCAT('%', :texto, '%')) OR
                       LOWER(k.producto.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')))
                ORDER BY k.movimiento.fecha DESC, k.idKardex DESC
            """)
    List<KardexJpaEntity> findAllWithFilters(
            @Param("tipoMovimiento") String tipoMovimiento,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("texto") String texto,
            Pageable pageable);

    @Query("""
                SELECT COUNT(k) FROM KardexJpaEntity k
                WHERE (:tipoMovimiento IS NULL OR :tipoMovimiento = '' OR k.movimiento.tipo = :tipoMovimiento
                       OR (:tipoMovimiento = 'AJUSTE' AND k.movimiento.tipo IN ('AJUSTE_POSITIVO', 'AJUSTE_NEGATIVO')))
                  AND (:fechaDesde IS NULL OR k.movimiento.fecha >= :fechaDesde)
                  AND (:fechaHasta IS NULL OR k.movimiento.fecha <= :fechaHasta)
                  AND (:texto IS NULL OR :texto = '' OR
                       LOWER(k.producto.cod_prod) LIKE LOWER(CONCAT('%', :texto, '%')) OR
                       LOWER(k.producto.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')))
            """)
    long countAllWithFilters(
            @Param("tipoMovimiento") String tipoMovimiento,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("texto") String texto);

    @Query("SELECT COUNT(k) FROM KardexJpaEntity k")
    long countAllKardex();
}
