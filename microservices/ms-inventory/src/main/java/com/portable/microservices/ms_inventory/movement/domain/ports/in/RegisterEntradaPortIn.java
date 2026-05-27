package com.portable.microservices.ms_inventory.movement.domain.ports.in;

import java.util.UUID;

import com.portable.microservices.ms_inventory.movement.domain.model.Movimiento;

public interface RegisterEntradaPortIn {
    /**
     * Registra una entrada de inventario creando un movimiento y actualizar el kardex
     * 
     * @param idLote ID del lote
     * @param idUsuario ID del usuario que registra
     * @param cantidad Cantidad de producto que ingresa
     * @param motivo Razón del movimiento
     * @param docRef Referencia a documento (ej: OC, picking order)
     * @return El movimiento creado
     */
    Movimiento execute(UUID idLote, Long idUsuario, Integer cantidad, String motivo, String docRef);
}
