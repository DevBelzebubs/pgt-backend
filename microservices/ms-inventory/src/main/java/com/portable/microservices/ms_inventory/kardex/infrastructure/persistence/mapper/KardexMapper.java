package com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;
import com.portable.microservices.ms_inventory.kardex.infrastructure.persistence.entity.KardexJpaEntity;
import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.entity.MovimientoJpaEntity;
import com.portable.microservices.ms_inventory.product.infrastructure.persistence.entity.ProductJpaEntity;

@Component
public class KardexMapper {
    public KardexJpaEntity toEntity(Kardex domain) {
        if (domain == null) return null;
        KardexJpaEntity entity = new KardexJpaEntity();
        if (domain.id() != null) entity.setIdKardex(domain.id());
        if (domain.movimientoId() != null) {
            MovimientoJpaEntity movimientoRef = new MovimientoJpaEntity();
            movimientoRef.setIdMovimiento(domain.movimientoId());
            entity.setMovimiento(movimientoRef);
        }
        if (domain.productoId() != null) {
            ProductJpaEntity productoRef = new ProductJpaEntity();
            productoRef.setId_producto(domain.productoId());
            entity.setProducto(productoRef);
        }
        entity.setStockAnterior(domain.stockAnterior());
        entity.setCantIngreso(domain.cantIngreso());
        entity.setCantSalida(domain.cantSalida());
        entity.setStockActual(domain.stockActual());
        entity.setCostoProm(domain.costoProm());
        return entity;
    }
    public Kardex toDomain(KardexJpaEntity entity) {
        if (entity == null) return null;
        UUID movimientoId = entity.getMovimiento() != null ? entity.getMovimiento().getIdMovimiento() : null;
        UUID productoId = entity.getProducto() != null ? entity.getProducto().getId_producto() : null;
        return new Kardex(
            entity.getIdKardex(),
            movimientoId,
            productoId,
            entity.getStockAnterior(),
            entity.getCantIngreso(),
            entity.getCantSalida(),
            entity.getStockActual(),
            entity.getCostoProm()
        );
    }
}