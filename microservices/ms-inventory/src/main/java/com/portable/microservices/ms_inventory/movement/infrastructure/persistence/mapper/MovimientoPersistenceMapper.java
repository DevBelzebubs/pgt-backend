package com.portable.microservices.ms_inventory.movement.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.movement.domain.model.Movimiento;
import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.entity.MovimientoJpaEntity;

@Component
public class MovimientoPersistenceMapper {

    public Movimiento toDomain(MovimientoJpaEntity entity) {
        if (entity == null) return null;
        
        return new Movimiento(
                entity.getIdMovimiento(),
                entity.getLote().getIdLote(),
                entity.getIdUsuario(),
                entity.getTipo(),
                entity.getFecha(),
                entity.getMotivo(),
                entity.getDocRef()
        );
    }

    public MovimientoJpaEntity toEntity(Movimiento domain) {
        if (domain == null) return null;
        
        MovimientoJpaEntity entity = new MovimientoJpaEntity();
        if (domain.idMovimiento() != null) {
            entity.setIdMovimiento(domain.idMovimiento());
        }
        entity.setIdUsuario(domain.idUsuario());
        entity.setTipo(domain.tipo());
        entity.setMotivo(domain.motivo());
        entity.setDocRef(domain.docRef());
        // Nota: el lote debe ser seteado en el adapter
        
        return entity;
    }
}
