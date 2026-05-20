package com.portable.microservices.ms_inventory.alert.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "inventory", name = "alerta_stock")
public class AlertJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_alerta", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "id_producto", nullable = false)
    private UUID productId;

    @Column(name = "stock_actual", nullable = false)
    private Integer currentStock;

    @Column(name = "stock_minimo", nullable = false)
    private Integer minStock;

    @Column(name = "estado", length = 20)
    private String status = "ACTIVA";

    @CreationTimestamp
    @Column(name = "fec_creacion", updatable = false)
    private OffsetDateTime createdAt;

    // No mapeamos el FK de producto con @ManyToOne si prefieres mantener 
    // integridad relacional, podrías añadir la relación aquí.
}