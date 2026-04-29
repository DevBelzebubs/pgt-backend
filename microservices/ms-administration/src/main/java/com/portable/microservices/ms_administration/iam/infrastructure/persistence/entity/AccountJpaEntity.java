package com.portable.microservices.ms_administration.iam.infrastructure.persistence.entity;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cuenta_usuario", schema = "administration")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private Long id;

    @Builder.Default
    @Column(name = "cuenta_uuid", unique = true, nullable = false, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UserJpaEntity user;

    @Column(name = "id_sede", nullable = false)
    private Long headquarterId;

    @Column(name = "nom_usu", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "contraseña", nullable = false)
    private String passwordHash;

    @Column(name = "estado")
    private boolean active;

    @CreationTimestamp
    @Column(name = "fec_creacion", updatable = false)
    private ZonedDateTime createdAt;
}
