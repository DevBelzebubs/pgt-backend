package com.portable.microservices.ms_inventory.lot.infrastructure.persistence.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.entity.LocationJpaEntity;
import com.portable.microservices.ms_inventory.lot.domain.model.Lot;
import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.entity.LoteJpaEntity;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.ProductJpaEntity;

@Component
public class LotMapper {
    public LoteJpaEntity toEntity(Lot domain) {
        if (domain == null) return null;
        LoteJpaEntity entity = new LoteJpaEntity();
        
        if (domain.id() != null) {
            entity.setIdLote(domain.id());
        }
        
        if (domain.productId() != null) {
            ProductJpaEntity productoRef = new ProductJpaEntity();
            productoRef.setId_producto(domain.productId());
            entity.setProducto(productoRef);
        }
        
        if (domain.locationId() != null) {
            LocationJpaEntity locacionRef = new LocationJpaEntity();
            locacionRef.setIdLocacion(domain.locationId());
            entity.setLocacion(locacionRef);
        }
        
        entity.setNroLote(domain.nroLote());
        entity.setFecIngreso(domain.fecIngreso());
        entity.setCostoUnit(domain.costoUnit());
        entity.setEstado(domain.estado() != null ? domain.estado() : "DISPONIBLE");
        
        entity.setProveedor(domain.proveedor());
        entity.setCodProv(domain.codProv());
        
        return entity;
    }
    public Lot toDomain(LoteJpaEntity entity) {
        if (entity == null) return null;
        UUID productId = entity.getProducto() != null ? entity.getProducto().getId_producto() : null;
        UUID locationId = entity.getLocacion() != null ? entity.getLocacion().getIdLocacion() : null;
        return new Lot(
            entity.getIdLote(),
            productId,
            locationId,
            entity.getNroLote(),
            entity.getFecIngreso(),
            entity.getCostoUnit(),
            entity.getEstado(),
            entity.getProveedor(),
            entity.getCodProv()
        );
    }
}