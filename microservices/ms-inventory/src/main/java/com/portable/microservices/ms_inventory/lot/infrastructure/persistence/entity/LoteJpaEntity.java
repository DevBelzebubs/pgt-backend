package com.portable.microservices.ms_inventory.lot.infrastructure.persistence.entity;

import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.entity.LocationJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Table(schema = "inventory", name = "lote")
public class LoteJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idLote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoJpaEntity producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_locacion", nullable = false)
    private LocationJpaEntity locacion;

    @Column(nullable = false, length = 50)
    private String nroLote;

    @Column(nullable = false)
    private LocalDate fecIngreso;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal costoUnit;

    @Column(length = 20)
    private String estado = "DISPONIBLE";

    public UUID getIdLote() {
        return idLote;
    }

    public void setIdLote(UUID idLote) {
        this.idLote = idLote;
    }

    public ProductoJpaEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoJpaEntity producto) {
        this.producto = producto;
    }

    public LocationJpaEntity getLocacion() {
        return locacion;
    }

    public void setLocacion(LocationJpaEntity locacion) {
        this.locacion = locacion;
    }

    public String getNroLote() {
        return nroLote;
    }

    public void setNroLote(String nroLote) {
        this.nroLote = nroLote;
    }

    public LocalDate getFecIngreso() {
        return fecIngreso;
    }

    public void setFecIngreso(LocalDate fecIngreso) {
        this.fecIngreso = fecIngreso;
    }

    public BigDecimal getCostoUnit() {
        return costoUnit;
    }

    public void setCostoUnit(BigDecimal costoUnit) {
        this.costoUnit = costoUnit;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}