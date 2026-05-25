package com.portable.microservices.ms_inventory.movement.application.usecases;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.entity.LoteJpaEntity;
import com.portable.microservices.ms_inventory.movement.domain.model.Movimiento;

import com.portable.microservices.ms_inventory.movement.domain.ports.in.RegisterEntradaPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.KardexPersistencePortOut;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.LotePersistencePortOut;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.MovimientoPersistencePortOut;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso para registrar una entrada de inventario.
 * 
 * Crea un registro de movimiento tipo INGRESO y genera un kardex con la cantidad ingresada.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterEntradaUseCase implements RegisterEntradaPortIn {

    private final MovimientoPersistencePortOut movimientoPersistence;
    private final KardexPersistencePortOut kardexPersistence;
    private final LotePersistencePortOut lotePersistence;

    @Override
    @Transactional
    public Movimiento execute(UUID idLote, Long idUsuario, Integer cantidad, String motivo, String docRef) {
        log.info("Iniciando registro de entrada para lote: {}, cantidad: {}, usuario: {}", idLote, cantidad, idUsuario);

        // Validar que el lote existe
        LoteJpaEntity lote = lotePersistence.findLoteById(idLote)
                .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado: " + idLote));

        // Validar cantidad
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        // Crear movimiento de tipo INGRESO
        Movimiento movimiento = Movimiento.crearEntrada(
                idLote,
                idUsuario,
                motivo != null ? motivo : "Entrada registrada",
                docRef
        );

        // Validar reglas de negocio
        if (!movimiento.isValidForCreation()) {
            throw new IllegalArgumentException("Datos inválidos para crear movimiento");
        }

        // Persistir movimiento
        Movimiento movimientoGuardado = movimientoPersistence.save(movimiento);
        log.info("Movimiento guardado con ID: {}", movimientoGuardado.idMovimiento());

        // Registrar entrada en kardex
        BigDecimal costoUnitario = lote.getCostoUnit();
        kardexPersistence.registrarEntrada(
                movimientoGuardado.idMovimiento(),
                lote.getProducto().getId_producto(),
                cantidad,
                costoUnitario
        );
        
        log.info("Entrada registrada exitosamente para lote: {}, cantidad: {}", idLote, cantidad);

        return movimientoGuardado;
    }
}
