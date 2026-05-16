package com.portable.microservices.ms_inventory.locations.domain.model;

import java.util.UUID;

import lombok.Builder;

@Builder(toBuilder = true)
public record Warehouse(
    Long id,
    Long idSede, // FK hacia ms-administration
    UUID almacenUuid,
    String codAlm,
    String nombre,
    String tipo,
    Boolean activo
) {
    // Regla de negocio básica para la creación
    public boolean isValidForCreation() {
        return idSede != null && nombre != null && !nombre.isBlank() && codAlm != null;
    }
}
