package com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.repository;

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
}
