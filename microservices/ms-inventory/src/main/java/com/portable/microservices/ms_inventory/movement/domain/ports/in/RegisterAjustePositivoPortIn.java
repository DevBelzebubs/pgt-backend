package com.portable.microservices.ms_inventory.movement.domain.ports.in;

import java.util.UUID;

import com.portable.microservices.ms_inventory.movement.domain.model.Movimiento;

public interface RegisterAjustePositivoPortIn {

    /**
     * Registra un ajuste positivo de inventario, incrementando el stock de un lote.
     * A diferencia de una entrada por compra, el motivo es obligatorio para justificar la corrección.
     *
     * @param idLote     ID del lote a ajustar
     * @param idUsuario  ID del usuario que realiza el ajuste
     * @param cantidad   Cantidad a incrementar (debe ser > 0)
     * @param motivo     Razón del ajuste (obligatorio, ej: "Diferencia en conteo físico")
     * @param docRef     Referencia a documento de soporte (opcional)
     * @return El movimiento de ajuste creado
     */
    Movimiento execute(UUID idLote, Long idUsuario, Integer cantidad, String motivo, String docRef);
}
