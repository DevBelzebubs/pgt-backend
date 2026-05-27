package com.portable.microservices.ms_inventory.movement.application.usecases;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portable.microservices.ms_inventory.kardex.domain.model.Kardex;
import com.portable.microservices.ms_inventory.kardex.domain.ports.out.KardexPersistencePortOut;
import com.portable.microservices.ms_inventory.kardex.domain.service.CostoPromedioCalculator;
import com.portable.microservices.ms_inventory.movement.domain.model.Movement;
import com.portable.microservices.ms_inventory.movement.domain.model.TipoMovimiento;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.RegisterMovementPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.MovementPersistencePortOut;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterMovementUseCase implements RegisterMovementPortIn {

    private final MovementPersistencePortOut movementPersistence;
    private final KardexPersistencePortOut kardexPersistence;
    private final CostoPromedioCalculator costoPromedioCalculator;

    @Override
    @Transactional
    public void execute(RegisterMovementCommand command) {
        if (command.cantidad() == null || command.cantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (command.tipo() == null || command.tipo().isBlank()) {
            throw new IllegalArgumentException("El tipo de movimiento es requerido");
        }

        String upperTipo = command.tipo().toUpperCase();
        if ("AJUSTE".equals(upperTipo)) {
            upperTipo = "AJUSTE_POSITIVO";
        }
        TipoMovimiento tipo;
        try {
            tipo = TipoMovimiento.valueOf(upperTipo);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de movimiento inválido: " + command.tipo());
        }

        if (command.lotId() == null && command.productId() == null) {
            throw new IllegalArgumentException("Se requiere idProducto o idLote");
        }

        Movement movement = new Movement(
            null,
            command.lotId(),
            command.userId(),
            tipo,
            command.cantidad(),
            OffsetDateTime.now(),
            command.motivo(),
            command.docRef()
        );

        Movement saved = movementPersistence.save(movement);

        if (command.productId() != null) {
            java.util.Optional<Kardex> ultimoKardex = kardexPersistence.findLastByProductId(command.productId());

            CostoPromedioCalculator.ResultadoCalculoPPP resultado;
            if (tipo == TipoMovimiento.INGRESO || tipo == TipoMovimiento.AJUSTE_POSITIVO) {
                resultado = costoPromedioCalculator.calcularParaIngreso(
                    ultimoKardex,
                    command.cantidad(),
                    command.costoUnit() != null ? command.costoUnit() : BigDecimal.ZERO
                );
            } else {
                resultado = costoPromedioCalculator.calcularParaSalida(
                    ultimoKardex,
                    command.cantidad()
                );
            }

            int cantIngreso = (tipo == TipoMovimiento.INGRESO || tipo == TipoMovimiento.AJUSTE_POSITIVO)
                ? command.cantidad() : 0;
            int cantSalida = (tipo == TipoMovimiento.SALIDA || tipo == TipoMovimiento.EGRESO
                || tipo == TipoMovimiento.AJUSTE_NEGATIVO)
                ? command.cantidad() : 0;

            Kardex kardex = new Kardex(
                null,
                saved.id(),
                command.productId(),
                resultado.stockAnterior(),
                cantIngreso,
                cantSalida,
                resultado.stockActual(),
                resultado.costoPromNuevo()
            );

            kardexPersistence.save(kardex);
        }
    }
}
