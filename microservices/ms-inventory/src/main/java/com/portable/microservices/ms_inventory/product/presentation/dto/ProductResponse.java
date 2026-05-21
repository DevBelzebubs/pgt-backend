package com.portable.microservices.ms_inventory.product.presentation.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public record ProductResponse(
    UUID id,
    Long categoryId,
    Long brandId,
    String codProd,
    String codAnexo,
    String descripcion,
    String modelosCompatibles,
    BigDecimal preCom,
    BigDecimal preVen,
    boolean estado,
    ZonedDateTime fecCreacion
) {}
