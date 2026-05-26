package com.portable.microservices.ms_inventory.movement.application.usecases;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.entity.LoteJpaEntity;
import com.portable.microservices.ms_inventory.movement.domain.model.Movimiento;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.RegisterSalidaPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.KardexPersistencePortOut;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.LotePersistencePortOut;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.MovimientoPersistencePortOut;
import com.portable.microservices.ms_inventory.shared.domain.event.StockDecreasedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso para registrar una salida de inventario.
 * 
 * Crea un registro de movimiento tipo SALIDA y genera un kardex con la cantidad egresada,
 * validando que exista suficiente stock disponible a nivel global para el producto.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterSalidaUseCase implements RegisterSalidaPortIn {

    private final MovimientoPersistencePortOut movimientoPersistence;
    private final KardexPersistencePortOut kardexPersistence;
    private final LotePersistencePortOut lotePersistence;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Movimiento execute(UUID idLote, Long idUsuario, Integer cantidad, String motivo, String docRef) {
        log.info("Iniciando registro de salida para lote: {}, cantidad: {}, usuario: {}", idLote, cantidad, idUsuario);

        // Validar que el lote existe
        LoteJpaEntity lote = lotePersistence.findLoteById(idLote)
                .orElseThrow(() -> new IllegalArgumentException("Lote no encontrado: " + idLote));

        // Validar cantidad
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de salida debe ser mayor a 0");
        }

        UUID idProducto = lote.getProducto().getId_producto();

        // Validar stock disponible
        Integer stockDisponible = kardexPersistence.getStockActual(idProducto);
        if (stockDisponible < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente para realizar la salida. Stock disponible: " + stockDisponible + ", solicitado: " + cantidad);
        }

        // Crear movimiento de tipo SALIDA
        Movimiento movimiento = Movimiento.crearSalida(
                idLote,
                idUsuario,
                motivo != null ? motivo : "Salida registrada",
                docRef
        );

        // Validar reglas de negocio del dominio
        if (!movimiento.isValidForCreation()) {
            throw new IllegalArgumentException("Datos inválidos para crear movimiento de salida");
        }

        // Persistir movimiento
        Movimiento movimientoGuardado = movimientoPersistence.save(movimiento);
        log.info("Movimiento de salida guardado con ID: {}", movimientoGuardado.idMovimiento());

        // Registrar salida en kardex
        BigDecimal costoUnitario = lote.getCostoUnit();
        kardexPersistence.registrarSalida(
                movimientoGuardado.idMovimiento(),
                idProducto,
                cantidad,
                costoUnitario
        );
        
        // Calcular el nuevo stock para publicar el evento
        Integer nuevoStock = stockDisponible - cantidad;
        log.info("Salida registrada exitosamente para lote: {}, cantidad: {}. Nuevo stock global: {}", idLote, cantidad, nuevoStock);

        // Publicar evento transaccional de decremento de stock
        eventPublisher.publishEvent(new StockDecreasedEvent(idProducto, nuevoStock));

        return movimientoGuardado;
    }
}
