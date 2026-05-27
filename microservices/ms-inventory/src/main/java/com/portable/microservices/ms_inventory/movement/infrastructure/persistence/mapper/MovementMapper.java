package com.portable.microservices.ms_inventory.movement.infrastructure.persistence.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.entity.LoteJpaEntity;
import com.portable.microservices.ms_inventory.movement.domain.model.Movement;
import com.portable.microservices.ms_inventory.movement.domain.model.TipoMovimiento;
import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.entity.MovimientoJpaEntity;

@Component
public class MovementMapper {
    public MovimientoJpaEntity toEntity(Movement domain) {
        if (domain == null) return null;
        MovimientoJpaEntity entity = new MovimientoJpaEntity();
        
        if (domain.id() != null) {
            entity.setIdMovimiento(domain.id());
        }
        
        if (domain.lotId() != null) {
            LoteJpaEntity loteRef = new LoteJpaEntity();
            loteRef.setIdLote(domain.lotId());
            entity.setLote(loteRef);
        }
        
        entity.setIdUsuario(domain.userId());
        entity.setTipo(domain.tipo() != null ? domain.tipo().name() : null);
        entity.setCantidad(domain.cantidad());
        entity.setMotivo(domain.motivo());
        entity.setDocRef(domain.docRef());
        
        return entity;
    }
    public Movement toDomain(MovimientoJpaEntity entity) {
        if (entity == null) return null;
        TipoMovimiento tipo = null;
        if (entity.getTipo() != null) {
            try {
                tipo = TipoMovimiento.valueOf(entity.getTipo().toUpperCase());
            } catch (IllegalArgumentException e) {
                tipo = null;
            }
        }
        UUID lotId = entity.getLote() != null ? entity.getLote().getIdLote() : null;
        return new Movement(
            entity.getIdMovimiento(),
            lotId,
            entity.getIdUsuario(),
            tipo,
            entity.getCantidad(),
            entity.getFecha(),
            entity.getMotivo(),
            entity.getDocRef()
        );
    }
}