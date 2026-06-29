package com.portable.microservices.ms_inventory.heatmap.infrastructure.presentation.dto;

import java.util.UUID;

public record HeatmapLocationResponse(
    UUID idLocacion,
    String zona,
    String pasillo,
    String estante,
    String codBarras,
    Integer capacidad,
    long movementCount,
    long dailyPicks,
    int intensity,
    String categoriaPrincipal
) {}
