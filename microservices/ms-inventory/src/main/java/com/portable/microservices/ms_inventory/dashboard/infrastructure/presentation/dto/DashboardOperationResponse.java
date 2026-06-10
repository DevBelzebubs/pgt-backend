package com.portable.microservices.ms_inventory.dashboard.infrastructure.presentation.dto;

public record DashboardOperationResponse(
    String id,
    String type,
    String product,
    String date,
    String user,
    String status
) {}
