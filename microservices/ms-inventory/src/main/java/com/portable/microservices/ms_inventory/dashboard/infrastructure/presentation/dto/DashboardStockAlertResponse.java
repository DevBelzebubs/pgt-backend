package com.portable.microservices.ms_inventory.dashboard.infrastructure.presentation.dto;

public record DashboardStockAlertResponse(
    String sku,
    String name,
    int stock,
    String status,
    double costoPromedio
) {}
