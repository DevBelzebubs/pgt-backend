package com.portable.microservices.ms_inventory.heatmap.infrastructure.presentation.dto;

import java.util.UUID;

public record LocationProductResponse(
    UUID idLote,
    String productoCod,
    String productoDesc,
    Integer cantidad,
    String nroLote
) {}
