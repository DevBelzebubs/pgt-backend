package com.portable.microservices.ms_inventory.kardex.application.dto;

import java.math.BigDecimal;

public record KardexExportRow(
    String fecha,
    String producto,
    String sku,
    String tipoMovimiento,
    String documentoRef,
    Integer stockAnterior,
    Integer cantIngreso,
    Integer cantSalida,
    Integer stockActual,
    BigDecimal costoPromedio
) {}
