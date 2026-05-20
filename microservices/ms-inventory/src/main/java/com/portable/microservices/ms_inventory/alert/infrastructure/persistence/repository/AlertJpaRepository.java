package com.portable.microservices.ms_inventory.alert.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portable.microservices.ms_inventory.alert.infrastructure.persistence.entity.AlertJpaEntity;

@Repository
public interface AlertJpaRepository extends JpaRepository<AlertJpaEntity, UUID> {
    boolean existsByProductIdAndStatus(UUID productId, String status);
    List<AlertJpaEntity> findByStatus(String status);
}
