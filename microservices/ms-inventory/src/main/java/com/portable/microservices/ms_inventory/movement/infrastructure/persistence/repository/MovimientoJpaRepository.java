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
                        "WHERE (:tipo IS NULL OR :tipo = '' OR m.tipo = :tipo OR (:tipo = 'AJUSTE' AND m.tipo IN ('AJUSTE_POSITIVO', 'AJUSTE_NEGATIVO'))) "
                        +
                        "AND (cast(:fechaDesde as date) IS NULL OR m.fecha >= :fechaDesde) " +
                        "AND (cast(:fechaHasta as date) IS NULL OR m.fecha <= :fechaHasta) " +
                        "AND (cast(:idProducto as uuid) IS NULL OR COALESCE(p.id_producto, kp.id_producto) = :idProducto) "
                        +
                        "AND (:texto IS NULL OR :texto = '' OR " +
                        "LOWER(COALESCE(p.cod_prod, kp.cod_prod)) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
                        "LOWER(COALESCE(p.descripcion, kp.descripcion)) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
                        "LOWER(m.docRef) LIKE LOWER(CONCAT('%', :texto, '%'))) " +
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
                                   OR LOWER(COALESCE(p.cod_prod, kp.cod_prod)) LIKE LOWER(CONCAT('%', :texto, '%'))
                                   OR LOWER(COALESCE(p.descripcion, kp.descripcion)) LIKE LOWER(CONCAT('%', :texto, '%'))
                                   OR LOWER(m.docRef) LIKE LOWER(CONCAT('%', :texto, '%')))
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
                                   LOWER(COALESCE(p.cod_prod, kp.cod_prod)) LIKE LOWER(CONCAT('%', :texto, '%')) OR
                                   LOWER(COALESCE(p.descripcion, kp.descripcion)) LIKE LOWER(CONCAT('%', :texto, '%')) OR
                                   LOWER(m.docRef) LIKE LOWER(CONCAT('%', :texto, '%')))
                            ORDER BY m.fecha DESC
                        """)
        List<Object[]> findAllWithFiltersPaged(
                        @Param("tipo") String tipo,
                        @Param("fechaDesde") LocalDate fechaDesde,
                        @Param("fechaHasta") LocalDate fechaHasta,
                        @Param("idProducto") UUID idProducto,
                        @Param("texto") String texto,
                        Pageable pageable);

        // --- Dashboard queries ---

        @Query(value = "SELECT COUNT(*) FROM inventory.movimiento WHERE tipo = 'SALIDA' AND EXTRACT(MONTH FROM fecha) = EXTRACT(MONTH FROM CURRENT_DATE) AND EXTRACT(YEAR FROM fecha) = EXTRACT(YEAR FROM CURRENT_DATE)", nativeQuery = true)
        long countSalidasEsteMes();

        @Query(value = "SELECT COUNT(*) FROM inventory.movimiento WHERE tipo = 'SALIDA' AND EXTRACT(MONTH FROM fecha) = :mes AND EXTRACT(YEAR FROM fecha) = :anio", nativeQuery = true)
        long countSalidasDelMes(@Param("mes") int mes, @Param("anio") int anio);

        @Query(value = "SELECT m.tipo, COUNT(*) as cnt FROM inventory.movimiento m GROUP BY m.tipo", nativeQuery = true)
        List<Object[]> countGroupByTipo();

        @Query(value = "SELECT EXTRACT(YEAR FROM m.fecha) as anio, EXTRACT(MONTH FROM m.fecha) as mes, m.tipo, COUNT(*) as cnt FROM inventory.movimiento m WHERE m.fecha >= :desde GROUP BY EXTRACT(YEAR FROM m.fecha), EXTRACT(MONTH FROM m.fecha), m.tipo ORDER BY anio, mes", nativeQuery = true)
        List<Object[]> findMovementsGroupedByMonth(@Param("desde") LocalDate desde);

        @Query(value = "SELECT m.id_movimiento, m.tipo, COALESCE(p.descripcion, ''), TO_CHAR(m.fecha, 'YYYY-MM-DD HH24:MI') as fecha_str, m.id_usuario, COALESCE(m.doc_ref, '') FROM inventory.movimiento m LEFT JOIN inventory.lote l ON l.id_lote = m.id_lote LEFT JOIN inventory.producto p ON p.id_producto = l.id_producto ORDER BY m.fecha DESC LIMIT 10", nativeQuery = true)
        List<Object[]> findTop10RecentWithProduct();

        @Query(value = "SELECT COALESCE(p.descripcion, ''), COUNT(*) as cnt FROM inventory.movimiento m LEFT JOIN inventory.lote l ON l.id_lote = m.id_lote LEFT JOIN inventory.producto p ON p.id_producto = l.id_producto WHERE m.tipo = 'SALIDA' GROUP BY p.descripcion ORDER BY cnt DESC LIMIT 10", nativeQuery = true)
        List<Object[]> findTopProductosSalidas();

        @Query(value = """
                            SELECT
                                l.id_locacion, l.zona, l.pasillo, l.estante, l.cod_barras,
                                COALESCE(stock.total_qty, 0), COALESCE(mov.daily_picks, 0),
                                COALESCE(cat.categoria, ''), COALESCE(l.capacidad, 0)
                            FROM inventory.locacion l
                            LEFT JOIN (
                                SELECT lo.id_locacion,
                                       SUM(lo.cantidad) AS total_qty
                                FROM inventory.lote lo
                                WHERE lo.estado = 'DISPONIBLE'
                                GROUP BY lo.id_locacion
                            ) stock ON stock.id_locacion = l.id_locacion
                            LEFT JOIN (
                                SELECT lo.id_locacion,
                                       COUNT(*) AS daily_picks
                                FROM inventory.movimiento m
                                JOIN inventory.lote lo ON lo.id_lote = m.id_lote
                                WHERE lo.id_locacion IS NOT NULL AND m.fecha >= CURRENT_DATE
                                GROUP BY lo.id_locacion
                            ) mov ON mov.id_locacion = l.id_locacion
                            LEFT JOIN LATERAL (
                                SELECT p.descripcion AS categoria
                                FROM inventory.lote lo
                                JOIN inventory.producto p ON p.id_producto = lo.id_producto
                                WHERE lo.id_locacion = l.id_locacion AND lo.estado = 'DISPONIBLE'
                                LIMIT 1
                            ) cat ON true
                            WHERE l.id_almacen = :idAlmacen AND l.activo = true
                            ORDER BY l.pasillo, l.estante
                        """, nativeQuery = true)
        List<Object[]> findHeatmapByAlmacen(@Param("idAlmacen") Long idAlmacen);

        @Query(value = "SELECT COUNT(*) FROM inventory.movimiento m JOIN inventory.lote lo ON lo.id_lote = m.id_lote WHERE lo.id_locacion = :idLocacion AND m.fecha >= CURRENT_DATE", nativeQuery = true)
        long countDailyMovementsByLocation(@Param("idLocacion") UUID idLocacion);
}