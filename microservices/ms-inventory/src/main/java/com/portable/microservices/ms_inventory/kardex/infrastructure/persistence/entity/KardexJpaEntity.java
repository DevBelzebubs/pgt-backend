package com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.util.UUID;


import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.entity.MovimientoJpaEntity;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.ProductJpaEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Entity
@Table(schema = "inventory", name = "kardex")
public class KardexJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idKardex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_movimiento", nullable = false)
    private MovimientoJpaEntity movimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductJpaEntity producto;

    @Column(nullable = false)
    private Integer stockAnterior = 0;

    @Column(nullable = false)
    private Integer cantIngreso = 0;

    @Column(nullable = false)
    private Integer cantSalida = 0;

    @Column(nullable = false)
    private Integer stockActual;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal costoProm;

    public UUID getIdKardex() {
        return idKardex;
    }

    public void setIdKardex(UUID idKardex) {
        this.idKardex = idKardex;
    }

    public MovimientoJpaEntity getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(MovimientoJpaEntity movimiento) {
        this.movimiento = movimiento;
    }

    public ProductJpaEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductJpaEntity producto) {
        this.producto = producto;
    }

    public Integer getStockAnterior() {
        return stockAnterior;
    }

    public void setStockAnterior(Integer stockAnterior) {
        this.stockAnterior = stockAnterior;
    }

    public Integer getCantIngreso() {
        return cantIngreso;
    }

    public void setCantIngreso(Integer cantIngreso) {
        this.cantIngreso = cantIngreso;
    }

    public Integer getCantSalida() {
        return cantSalida;
    }

    public void setCantSalida(Integer cantSalida) {
        this.cantSalida = cantSalida;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public BigDecimal getCostoProm() {
        return costoProm;
    }

    public void setCostoProm(BigDecimal costoProm) {
        this.costoProm = costoProm;
    }
    
}