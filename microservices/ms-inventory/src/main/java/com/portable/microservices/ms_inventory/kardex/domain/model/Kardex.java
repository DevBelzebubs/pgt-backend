package com.portable.microservices.ms_inventory.kardex.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record Kardex(
    UUID id,
    UUID movimientoId,
    UUID productoId,
    Integer stockAnterior,
    Integer cantIngreso,
    Integer cantSalida,
    Integer stockActual,
    BigDecimal costoProm
) {}