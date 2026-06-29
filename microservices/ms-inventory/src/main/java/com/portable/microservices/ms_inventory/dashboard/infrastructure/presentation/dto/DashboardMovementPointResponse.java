package com.portable.microservices.ms_inventory.dashboard.infrastructure.presentation.dto;

public record DashboardMovementPointResponse(
    String date,
    String label,
    long ingresos,
    long salidas,
    long ajustes
) {}
