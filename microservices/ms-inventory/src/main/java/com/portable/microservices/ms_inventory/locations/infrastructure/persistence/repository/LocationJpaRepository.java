package com.portable.microservices.ms_inventory.locations.infrastructure.persistence.repository;

import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.entity.LocationJpaEntity;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationJpaRepository extends JpaRepository<LocationJpaEntity, UUID> {
    
    boolean existsByCodBarras(String codBarras);
    List<LocationJpaEntity> findByActivoTrue();
}
