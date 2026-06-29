package com.portable.microservices.ms_inventory.locations.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.entity.WarehouseJpaEntity;

@Repository
public interface WarehouseJpaRepository extends JpaRepository<WarehouseJpaEntity, Long>{

    boolean existsByCodAlm(String codAlm);
    
    List<WarehouseJpaEntity> findByActivoTrue();

}
