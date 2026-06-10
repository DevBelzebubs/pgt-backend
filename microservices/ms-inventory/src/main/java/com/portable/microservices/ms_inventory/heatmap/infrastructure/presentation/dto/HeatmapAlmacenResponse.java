package com.portable.microservices.ms_inventory.heatmap.infrastructure.presentation.dto;

import java.util.List;

public record HeatmapAlmacenResponse(
    AlmacenInfo almacen,
    List<HeatmapLocationResponse> locaciones
) {
    public record AlmacenInfo(Long id, String nombre, String codAlm) {}
}
