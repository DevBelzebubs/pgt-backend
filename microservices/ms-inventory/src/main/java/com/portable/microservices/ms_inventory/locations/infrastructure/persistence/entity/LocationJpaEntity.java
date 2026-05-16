package com.portable.microservices.ms_inventory.locations.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "locacion", schema = "inventory")
@Getter
@Setter
public class LocationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_locacion", updatable = false)
    private UUID idLocacion;

    @Column(name = "id_almacen", nullable = false)
    private Long idAlmacen;

    private String zona;
    private String pasillo;
    private String estante;

    @Column(name = "cod_barras", unique = true)
    private String codBarras;

    private Integer capacidad;
    private Boolean activo = true;
}
