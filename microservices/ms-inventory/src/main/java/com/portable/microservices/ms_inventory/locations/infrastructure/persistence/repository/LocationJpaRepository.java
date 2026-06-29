package com.portable.microservices.ms_inventory.locations.infrastructure.persistence.repository;

import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.entity.LocationJpaEntity;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationJpaRepository extends JpaRepository<LocationJpaEntity, UUID> {
    
    boolean existsByCodBarras(String codBarras);
    List<LocationJpaEntity> findByActivoTrue();

    @Query(value = "SELECT l.zona, COALESCE(l.capacidad, 0), COUNT(lo.id_lote) FROM inventory.locacion l LEFT JOIN inventory.lote lo ON lo.id_locacion = l.id_locacion AND lo.estado = 'DISPONIBLE' WHERE l.activo = true GROUP BY l.zona, l.capacidad ORDER BY l.zona", nativeQuery = true)
    List<Object[]> findZoneCapacities();
}
