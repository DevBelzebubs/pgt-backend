package com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.entity.KardexJpaEntity;

@Repository
public interface KardexJpaRepository extends JpaRepository<KardexJpaEntity, UUID> {
    @Query("SELECT k FROM KardexJpaEntity k WHERE k.producto.id_producto = :productId ORDER BY k.idKardex DESC")
    List<KardexJpaEntity> findByProductoIdOrderByIdKardexDesc(@Param("productId") UUID productId);
    default Optional<KardexJpaEntity> findTopByProductoIdOrderByIdKardexDesc(UUID productId) {
        List<KardexJpaEntity> lista = findByProductoIdOrderByIdKardexDesc(productId);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }
    @Query("SELECT k FROM KardexJpaEntity k WHERE k.producto.id_producto = :productId ORDER BY k.idKardex ASC")
    List<KardexJpaEntity> findByProductoIdOrderByIdKardexAsc(@Param("productId") UUID productId);
}
