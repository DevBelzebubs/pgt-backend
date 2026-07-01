package com.portable.microservices.ms_inventory.product.presentation.dto;

public record CategoryResponse(
    Long id,
    String name,
    String description
) {}
