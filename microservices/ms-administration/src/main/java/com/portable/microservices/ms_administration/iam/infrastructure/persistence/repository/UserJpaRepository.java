package com.portable.microservices.ms_administration.iam.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portable.microservices.ms_administration.iam.infrastructure.persistence.entity.UserJpaEntity;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    Optional<UserJpaEntity> findByDni(String dni);
    void deleteByUuid(String uuid);
}
