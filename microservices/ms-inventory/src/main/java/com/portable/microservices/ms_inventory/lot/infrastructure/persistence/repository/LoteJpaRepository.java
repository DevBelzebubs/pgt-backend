package com.portable.microservices.ms_inventory.lot.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.entity.LoteJpaEntity;

@Repository
public interface LoteJpaRepository extends JpaRepository<LoteJpaEntity, UUID> {

    @Query("SELECT l FROM LoteJpaEntity l WHERE l.producto.id_producto = :idProducto AND l.locacion.idLocacion = :idLocacion ORDER BY l.fecIngreso ASC")
    List<LoteJpaEntity> findByProductoAndLocacionOrderByFecIngresoAsc(@Param("idProducto") UUID idProducto, @Param("idLocacion") UUID idLocacion);

    @Query("SELECT l FROM LoteJpaEntity l WHERE l.producto.id_producto = :idProducto ORDER BY l.fecIngreso ASC")
    List<LoteJpaEntity> findByProductoIdOrderByFecIngresoAsc(@Param("idProducto") UUID idProducto);
}
