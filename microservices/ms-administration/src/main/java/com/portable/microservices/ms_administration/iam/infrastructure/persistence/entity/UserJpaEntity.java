package com.portable.microservices.ms_administration.iam.infrastructure.persistence.entity;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario", schema = "administration")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Builder.Default
    @Column(name = "usuario_uuid", unique = true, nullable = false, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "nombre", nullable = false, length = 80)
    private String firstName;

    @Column(name = "apellido", nullable = false, length = 80)
    private String lastName;

    @Column(name = "dni", unique = true, nullable = false, length = 8)
    private String dni;

    @CreationTimestamp
    @Column(name = "fec_creacion", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "actualizado_en")
    private ZonedDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_rol",
        schema = "administration",
        joinColumns = @JoinColumn(name = "id_usuario"),
        inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    @Builder.Default
    private Set<RoleJpaEntity> roles = new HashSet<>();
}
