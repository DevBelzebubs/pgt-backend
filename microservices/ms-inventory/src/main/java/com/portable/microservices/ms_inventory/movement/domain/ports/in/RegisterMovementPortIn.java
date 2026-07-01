package com.portable.microservices.ms_inventory.movement.domain.ports.in;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface RegisterMovementPortIn {
    record RegisterMovementCommand(
        String tipo,
        UUID productId,
        UUID lotId,
        UUID locationId,
        Integer cantidad,
        String motivo,
        String docRef,
        String proveedor,
        String nroLote,
        BigDecimal costoUnit,
        LocalDate fecGarantia,
        Long userId
    ) {}
    void execute(RegisterMovementCommand command);
}
