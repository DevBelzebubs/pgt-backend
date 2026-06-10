package com.portable.microservices.ms_inventory.dashboard.infrastructure.presentation.dto;

public record DashboardTipoProporcionResponse(
    String label,
    long value,
    String color
) {}
