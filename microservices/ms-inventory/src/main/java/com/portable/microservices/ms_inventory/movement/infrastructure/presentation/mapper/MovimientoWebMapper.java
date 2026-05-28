package com.portable.microservices.ms_inventory.movement.infrastructure.presentation.mapper;


import java.util.Map;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.movement.domain.model.Movimiento;
import com.portable.microservices.ms_inventory.movement.infrastructure.persistence.entity.MovimientoJpaEntity;
import com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto.MovimientoListadoResponse;
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
                movimiento.cantidad(),
                movimiento.fecha(),
                movimiento.motivo(),
                movimiento.docRef()
        );
    }

    public MovimientoListadoResponse toListadoResponse(Object[] row, Map<Long, String> userNames) {
        if (row == null || row.length < 5) return null;

        MovimientoJpaEntity m = (MovimientoJpaEntity) row[0];
        String nroLote = (String) row[1];
        String sku = (String) row[2];
        String producto = (String) row[3];
        String locacion = (String) row[4];
        Integer stockActual = 0;

        String tipo = m.getTipo();
        Integer cantidad = m.getCantidad() != null ? m.getCantidad() : 0;

        int cantIngreso = 0;
        int cantSalida = 0;
        if (tipo != null) {
            String upperTipo = tipo.toUpperCase();
            if (upperTipo.contains("INGRESO") || upperTipo.contains("ENTRADA")
                    || upperTipo.contains("AJUSTE_POSITIVO") || "AJUSTE".equals(upperTipo)) {
                cantIngreso = cantidad;
            } else if (upperTipo.contains("SALIDA") || upperTipo.contains("EGRESO")
                    || upperTipo.contains("AJUSTE_NEGATIVO")) {
                cantSalida = cantidad;
            }
        }
        String usuario = userNames.getOrDefault(m.getIdUsuario(), m.getIdUsuario() != null ? String.valueOf(m.getIdUsuario()) : "");
        return new MovimientoListadoResponse(
                m.getIdMovimiento(),
                tipo,
                m.getFecha(),
                m.getMotivo(),
                m.getDocRef(),
                nroLote,
                producto,
                sku,
                cantIngreso,
                cantSalida,
                stockActual,
                locacion,
                usuario
        );
    }
    public MovimientoListadoResponse toListadoResponse(Object[] row) {
    return toListadoResponse(row, Map.of());
}
}
