package com.portable.microservices.ms_inventory.movement.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.entity.MovimientoJpaEntity;

@Repository
public interface MovementJpaRepository extends JpaRepository<MovimientoJpaEntity, UUID> {
}