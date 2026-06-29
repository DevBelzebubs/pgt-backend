package com.portable.microservices.ms_inventory.movement.infrastructure.presentation.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegisterAjustePositivoRequest(

        @NotNull(message = "El ID del lote es obligatorio")
        UUID idLote,

        @NotNull(message = "El ID del usuario es obligatorio")
        Long idUsuario,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a 0")
        Integer cantidad,

        /**
         * En ajustes el motivo es obligatorio — debe justificarse la corrección.
         * Ejemplo: "Diferencia encontrada en conteo físico", "Error de registro previo"
         */
        @NotBlank(message = "El motivo es obligatorio para un ajuste positivo")
        String motivo,

        String docRef
) {
}
