package com.portable.microservices.ms_inventory.dashboard.infrastructure.presentation.dto;

import java.util.List;

public record DashboardKpiResponse(
    String id,
    String title,
    long value,
    String formattedValue,
    double trend,
    String trendLabel,
    boolean isPositive,
    String icon,
    List<Integer> sparkline
) {}