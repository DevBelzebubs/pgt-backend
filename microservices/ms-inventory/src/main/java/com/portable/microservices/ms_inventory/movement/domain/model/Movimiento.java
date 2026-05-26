package com.portable.microservices.ms_inventory.movement.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Movimiento(
    UUID idMovimiento,
    UUID idLote,
    Long idUsuario,
    String tipo,
    OffsetDateTime fecha,
    String motivo,
    String docRef
) {
    public boolean isValidForCreation() {
        return idLote != null && idUsuario != null && tipo != null && !tipo.isBlank();
    }

    public static Movimiento crearEntrada(UUID idLote, Long idUsuario, String motivo, String docRef) {
        return new Movimiento(
                null,
                idLote,
                idUsuario,
                TipoMovimiento.INGRESO.name(),
                null,
                motivo,
                docRef
        );
    }

    public static Movimiento crearAjustePositivo(UUID idLote, Long idUsuario, String motivo, String docRef) {
        return new Movimiento(
                null,
                idLote,
                idUsuario,
                TipoMovimiento.AJUSTE_POSITIVO.name(),
                null,
                motivo,
                docRef
        );
    }

    public static Movimiento crearSalida(UUID idLote, Long idUsuario, String motivo, String docRef) {
        return new Movimiento(
                null,
                idLote,
                idUsuario,
                TipoMovimiento.SALIDA.name(),
                null,
                motivo,
                docRef
        );
    }
}
