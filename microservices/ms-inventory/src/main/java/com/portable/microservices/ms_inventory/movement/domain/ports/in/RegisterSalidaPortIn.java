package com.portable.microservices.ms_inventory.movement.domain.ports.in;

import java.util.UUID;

import com.portable.microservices.ms_inventory.movement.domain.model.Movimiento;

public interface RegisterSalidaPortIn {
    /**
     * Registra una salida de inventario creando un movimiento y actualizando el kardex
     * 
     * @param idLote ID del lote
     * @param idUsuario ID del usuario que registra
     * @param cantidad Cantidad de producto que sale
     * @param motivo Razón del movimiento
     * @param docRef Referencia a documento (ej: OC, picking order)
     * @return El movimiento creado
     */
    Movimiento execute(UUID idLote, Long idUsuario, Integer cantidad, String motivo, String docRef);
}
