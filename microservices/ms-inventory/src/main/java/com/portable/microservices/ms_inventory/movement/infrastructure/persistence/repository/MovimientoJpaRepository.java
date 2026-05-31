package com.portable.microservices.ms_inventory.movement.infrastructure.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.entity.MovimientoJpaEntity;

@Repository
public interface MovimientoJpaRepository extends JpaRepository<MovimientoJpaEntity, UUID> {
    
    List<MovimientoJpaEntity> findByLote_IdLote(UUID idLote);

    @Query("""
                SELECT m, COALESCE(l.nroLote,''),
                  COALESCE(p.cod_prod, kp.cod_prod),
                  COALESCE(p.descripcion, kp.descripcion),
                  COALESCE(lc.codBarras,'')
                FROM MovimientoJpaEntity m
                LEFT JOIN m.lote l
                LEFT JOIN l.producto p
                LEFT JOIN l.locacion lc
                LEFT JOIN KardexJpaEntity k ON k.movimiento = m
                LEFT JOIN k.producto kp
                ORDER BY m.fecha DESC
            """)
    List<Object[]> findAllWithDetails();

    @Query("""
                SELECT m, COALESCE(l.nroLote,''),
                  COALESCE(p.cod_prod, kp.cod_prod),
                  COALESCE(p.descripcion, kp.descripcion),
                  COALESCE(lc.codBarras,'')
                FROM MovimientoJpaEntity m
                LEFT JOIN m.lote l
                LEFT JOIN l.producto p
                LEFT JOIN l.locacion lc
                LEFT JOIN KardexJpaEntity k ON k.movimiento = m
                LEFT JOIN k.producto kp
                WHERE m.idMovimiento = :id
            """)
    List<Object[]> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT m, COALESCE(l.nroLote,''), " +
            "COALESCE(p.cod_prod, kp.cod_prod), " +
            "COALESCE(p.descripcion, kp.descripcion), " +
            "COALESCE(lc.codBarras,'') FROM MovimientoJpaEntity m " +
            "LEFT JOIN m.lote l " +
            "LEFT JOIN l.producto p " +
            "LEFT JOIN l.locacion lc " +
            "LEFT JOIN KardexJpaEntity k ON k.movimiento = m " +
            "LEFT JOIN k.producto kp " +
            "WHERE (:tipo IS NULL OR :tipo = '' OR m.tipo = :tipo OR (:tipo = 'AJUSTE' AND m.tipo IN ('AJUSTE_POSITIVO', 'AJUSTE_NEGATIVO'))) " +
            "AND (cast(:fechaDesde as date) IS NULL OR m.fecha >= :fechaDesde) " +
            "AND (cast(:fechaHasta as date) IS NULL OR m.fecha <= :fechaHasta) " +
            "AND (cast(:idProducto as uuid) IS NULL OR COALESCE(p.id_producto, kp.id_producto) = :idProducto) " +
            "AND (:texto IS NULL OR :texto = '' OR " +
            "LOWER(COALESCE(p.cod_prod, kp.cod_prod)) LIKE LOWER(CONCAT('%', cast(:texto as string), '%')) OR " +
            "LOWER(COALESCE(p.descripcion, kp.descripcion)) LIKE LOWER(CONCAT('%', cast(:texto as string), '%')) OR " +
            "LOWER(m.docRef) LIKE LOWER(CONCAT('%', cast(:texto as string), '%'))) " +
            "ORDER BY m.fecha DESC")
    List<Object[]> findAllWithFilters(
            @Param("tipo") String tipo,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("idProducto") UUID idProducto,
            @Param("texto") String texto);

    @Query("""
                SELECT COUNT(m)
                FROM MovimientoJpaEntity m
                LEFT JOIN m.lote l
                LEFT JOIN l.producto p
                LEFT JOIN KardexJpaEntity k ON k.movimiento = m
                LEFT JOIN k.producto kp
                WHERE (:tipo IS NULL OR :tipo = '' OR m.tipo = :tipo
                       OR (:tipo = 'AJUSTE' AND m.tipo IN ('AJUSTE_POSITIVO', 'AJUSTE_NEGATIVO')))
                  AND (cast(:fechaDesde as date) IS NULL OR m.fecha >= :fechaDesde)
                  AND (cast(:fechaHasta as date) IS NULL OR m.fecha <= :fechaHasta)
                  AND (cast(:idProducto as uuid) IS NULL OR COALESCE(p.id_producto, kp.id_producto) = :idProducto)
                  AND (:texto IS NULL OR :texto = '' 
                       OR LOWER(COALESCE(p.cod_prod, kp.cod_prod)) LIKE LOWER(CONCAT('%', cast(:texto as string), '%'))
                       OR LOWER(COALESCE(p.descripcion, kp.descripcion)) LIKE LOWER(CONCAT('%', cast(:texto as string), '%'))
                       OR LOWER(m.docRef) LIKE LOWER(CONCAT('%', cast(:texto as string), '%')))
            """)
    long countAllWithFilters(@Param("tipo") String tipo,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("idProducto") UUID idProducto,
            @Param("texto") String texto);

    @Query("""
                SELECT m, COALESCE(l.nroLote,''),
                  COALESCE(p.cod_prod, kp.cod_prod),
                  COALESCE(p.descripcion, kp.descripcion),
                  COALESCE(lc.codBarras,'')
                FROM MovimientoJpaEntity m
                LEFT JOIN m.lote l
                LEFT JOIN l.producto p
                LEFT JOIN l.locacion lc
                LEFT JOIN KardexJpaEntity k ON k.movimiento = m
                LEFT JOIN k.producto kp
                WHERE (:tipo IS NULL OR :tipo = '' OR m.tipo = :tipo
                       OR (:tipo = 'AJUSTE' AND m.tipo IN ('AJUSTE_POSITIVO', 'AJUSTE_NEGATIVO')))
                  AND (cast(:fechaDesde as date) IS NULL OR m.fecha >= :fechaDesde)
                  AND (cast(:fechaHasta as date) IS NULL OR m.fecha <= :fechaHasta)
                  AND (cast(:idProducto as uuid) IS NULL OR COALESCE(p.id_producto, kp.id_producto) = :idProducto)
                  AND (:texto IS NULL OR :texto = '' OR
                       LOWER(COALESCE(p.cod_prod, kp.cod_prod)) LIKE LOWER(CONCAT('%', cast(:texto as string), '%')) OR
                       LOWER(COALESCE(p.descripcion, kp.descripcion)) LIKE LOWER(CONCAT('%', cast(:texto as string), '%')) OR
                       LOWER(m.docRef) LIKE LOWER(CONCAT('%', cast(:texto as string), '%')))
                ORDER BY m.fecha DESC
            """)
    List<Object[]> findAllWithFiltersPaged(
            @Param("tipo") String tipo,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("idProducto") UUID idProducto,
            @Param("texto") String texto,
            Pageable pageable);
}