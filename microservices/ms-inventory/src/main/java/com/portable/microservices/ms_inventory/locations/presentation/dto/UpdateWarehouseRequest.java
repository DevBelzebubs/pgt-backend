package com.portable.microservices.ms_inventory.locations.presentation.dto;


public record UpdateWarehouseRequest(
    String codAlm,
    String nombre,
    String tipo
) {
}