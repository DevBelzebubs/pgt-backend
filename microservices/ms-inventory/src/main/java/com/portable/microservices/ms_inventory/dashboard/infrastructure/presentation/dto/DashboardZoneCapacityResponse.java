package com.portable.microservices.ms_inventory.dashboard.infrastructure.presentation.dto;

public record DashboardZoneCapacityResponse(
    String zone,
    long used,
    long total,
    double percentage
) {}
