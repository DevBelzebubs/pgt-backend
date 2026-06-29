package com.portable.microservices.ms_inventory.alert.infrastructure.persistence.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.portable.microservices.ms_inventory.alert.domain.enums.StockAlertStatus;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.ProductJpaEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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
@Table(name = "alerta_stock", schema = "inventory")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_alerta", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_producto",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_alert")
    )
    private ProductJpaEntity product;

    @Column(name = "stock_actual", nullable = false)
    private Integer realStock;

    @Column(name = "stock_minimo", nullable = false)
    private Integer minimunStock;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private StockAlertStatus status;

    @Column(name = "fec_creacion", updatable = false)
    private OffsetDateTime creationDate;

}
