package com.portable.microservices.ms_inventory.product.domain.model;

public record Category(
        Long id,
        String categoryName,
        String description
) {
}