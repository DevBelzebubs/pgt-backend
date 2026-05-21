package com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "producto", schema = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductJpaEntity {

    @Id
    @Column(name = "id_producto", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "id_categoria", nullable = false)
    private Long categoryId;

    @Column(name = "id_marca", nullable = false)
    private Long brandId;

    @Column(name = "cod_prod", unique = true, nullable = false, length = 30)
    private String codProd;

    @Column(name = "cod_anexo", length = 30)
    private String codAnexo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "modelos_compatibles")
    private String modelosCompatibles;

    @Column(name = "pre_com", nullable = false)
    private BigDecimal preCom;

    @Column(name = "pre_ven", nullable = false)
    private BigDecimal preVen;

    @Column(name = "estado")
    private boolean estado;

    @CreationTimestamp
    @Column(name = "fec_creacion", updatable = false)
    private ZonedDateTime fecCreacion;
}
