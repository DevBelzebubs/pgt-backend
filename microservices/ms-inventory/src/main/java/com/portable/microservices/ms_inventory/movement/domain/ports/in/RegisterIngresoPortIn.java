package com.portable.microservices.ms_inventory.movement.domain.ports.in;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface RegisterIngresoPortIn {
    record RegisterIngresoCommand(
        UUID productId,
        UUID locationId,
        String nroLote,
        LocalDate fecIngreso,
        BigDecimal costoUnit,
        Integer cantidad,
        Long userId,
        String motivo,
        String docRef,
        String proveedor,
        String codProv,
        LocalDate fecGarantia
    ) {}
    void execute(RegisterIngresoCommand command);
}