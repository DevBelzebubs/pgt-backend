package com.portable.microservices.ms_inventory.lot.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.entity.LoteJpaEntity;

@Repository
public interface LoteJpaRepository extends JpaRepository<LoteJpaEntity, UUID> {
}
