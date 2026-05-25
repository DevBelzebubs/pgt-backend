package com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.entity.KardexJpaEntity;

@Repository
public interface KardexJpaRepository extends JpaRepository<KardexJpaEntity, UUID> {
}
