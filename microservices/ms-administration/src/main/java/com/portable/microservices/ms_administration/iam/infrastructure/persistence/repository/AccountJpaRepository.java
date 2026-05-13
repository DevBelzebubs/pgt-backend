package com.portable.microservices.ms_administration.iam.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portable.microservices.ms_administration.iam.infrastructure.persistence.entity.AccountJpaEntity;

public interface AccountJpaRepository extends JpaRepository<AccountJpaEntity, Long> {
    Optional<AccountJpaEntity> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<AccountJpaEntity> findByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);
}
