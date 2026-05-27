package com.portable.microservices.ms_inventory.lot.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record Lot(
    UUID id,
    UUID productId,
    UUID locationId,
    String nroLote,
    LocalDate fecIngreso,
    BigDecimal costoUnit,
    String estado,
    String proveedor,
    String codProv
){}