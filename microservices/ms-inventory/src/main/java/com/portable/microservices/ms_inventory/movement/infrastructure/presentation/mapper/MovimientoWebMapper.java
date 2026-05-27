package com.portable.microservices.ms_inventory.movement.infrastructure.presentation.mapper;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.movement.domain.model.Movimiento;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.MovimientoResponse;

@Component
public class MovimientoWebMapper {

    public MovimientoResponse toResponse(Movimiento movimiento) {
        if (movimiento == null) return null;
        
        return new MovimientoResponse(
                movimiento.idMovimiento(),
                movimiento.idLote(),
                movimiento.idUsuario(),
                movimiento.tipo(),
                movimiento.fecha(),
                movimiento.motivo(),
                movimiento.docRef()
        );
    }
}
