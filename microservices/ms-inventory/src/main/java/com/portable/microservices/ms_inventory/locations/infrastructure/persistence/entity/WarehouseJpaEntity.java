package com.portable.microservices.ms_inventory.locations.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.UUID;

@Entity
@Table(name = "almacen", schema = "inventory")
@Getter
@Setter
public class WarehouseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_almacen")
    private Long id;

    @Column(name = "id_sede", nullable = false)
    private Long idSede;

    @Column(name = "almacen_uuid", updatable = false)
    private UUID almacenUuid;

    @Column(name = "cod_alm")
    private String codAlm;

    private String nombre;
    private String tipo;
    private Boolean activo = true;

    @PrePersist
    public void prePersist() {
        if (almacenUuid == null) {
            almacenUuid = UUID.randomUUID();
        }
    }
}