package com.portable.microservices.ms_administration.iam.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portable.microservices.ms_administration.iam.infrastructure.persistence.entity.RoleJpaEntity;

public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, Long> {
    Optional<RoleJpaEntity> findByName(String name);
}
