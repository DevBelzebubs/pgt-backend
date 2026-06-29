package com.portable.microservices.ms_inventory.product.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record BrandRequest(
    @NotBlank String name
) {}
