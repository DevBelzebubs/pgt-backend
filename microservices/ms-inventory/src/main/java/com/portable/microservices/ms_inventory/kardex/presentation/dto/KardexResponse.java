package com.portable.microservices.ms_inventory.kardex.presentation.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.portable.microservices.ms_inventory.kardex.domain.model.MetodoCosto;

public record KardexResponse(
    UUID idKardex,
    UUID idMovimiento,
    UUID idProducto,
    String producto,
    String sku,
    String fecha,
    String tipoMovimiento,
    String documentoRef,
    Integer stockAnterior,
    Integer cantIngreso,
    Integer cantSalida,
    Integer stockActual,
    BigDecimal costoPromedio,
    MetodoCosto metodoCosto
) {}