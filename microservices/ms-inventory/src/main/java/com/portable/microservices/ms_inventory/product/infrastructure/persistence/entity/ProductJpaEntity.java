package com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.Type;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "producto", schema = "inventory", uniqueConstraints = {
    @UniqueConstraint(columnNames = "uk_product_name"),
    @UniqueConstraint(columnNames = "ccod_prod")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductJpaEntity {
    
    @Id
    @GeneratedValue
    @Column(name = "id_producto", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_categoria",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_product_category")
    )
    private CategoryJpaEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_marca",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_product_brand")
    )
    private BrandJpaEntity brand;

    @Column(name = "cod_prod", nullable = false, length = 30)
    private String productCode;

    @Column(name = "cod_anexo", length = 30)
    private String manufactureCode;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Type(JsonBinaryType.class)
    @Column(
            name = "modelos_compatibles",
            columnDefinition = "jsonb"
    )
    private List<String> compatibleModels;

    @Column(name = "pre_com", precision = 12, scale = 4, nullable = false)
    private BigDecimal purchasePrice;

    @Column(name = "pre_ven", precision = 12, scale = 4, nullable = false)
    private BigDecimal salePrice;

    @Column(name = "estado")
    private Boolean status;

    @Column(name = "fec_creacion", updatable = false)
    private OffsetDateTime creationDate;

    @Column(name = "stock_minimo", nullable = false)
    private Integer minimunStock;
}
