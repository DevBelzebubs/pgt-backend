package com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegisterSalidaRequest(
    @NotNull(message = "El ID del lote es obligatorio")
    UUID idLote,
    
    @NotNull(message = "El ID del usuario es obligatorio")
    Long idUsuario,
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    Integer cantidad,
    
    String motivo,
    
    String docRef
) {
}
