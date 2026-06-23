package com.portable.microservices.ms_tracking.picking.infrastructure.messaging.listener;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.portable.microservices.ms_tracking.heatmap.infrastructure.messaging.dto.MovementCreatedMessage;
import com.portable.microservices.ms_tracking.picking.domain.model.DetallePick;
import com.portable.microservices.ms_tracking.picking.domain.ports.in.CrearOrdenPickPortIn;
import com.portable.microservices.ms_tracking.picking.domain.ports.in.OptimizarRutaPortIn;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PickingFromSalidaEventListener {

    private final CrearOrdenPickPortIn crearOrdenPick;
    private final OptimizarRutaPortIn optimizarRuta;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "tracking.picking.salida.queue", durable = "true"), exchange = @Exchange(value = "inventory.exchange", type = "topic"), key = "inventory.movement.created"))
    public void handleSalidaCreada(MovementCreatedMessage message) {
        if (!"SALIDA".equals(message.tipoMovimiento()) && !"AJUSTE_NEGATIVO".equals(message.tipoMovimiento())) {
            return;
        }

        log.info("Movimiento tipo {} detectado, creando orden de picking. Movimiento ID: {}, Usuario: {}",
                message.tipoMovimiento(), message.movementId(), message.userId());

        if (message.locacionId() == null) {
            log.warn("{} {} sin locación, se omite creación de picking.", message.tipoMovimiento(), message.movementId());
            return;
        }

        var item = DetallePick.builder()
                .productoId(message.productId())
                .locacionId(message.locacionId())
                .cantRequerida(message.cantidad())
                .cantSeleccion(0)
                .estado("PENDIENTE")
                .build();

        try {
            var orden = crearOrdenPick.execute(
                    message.userId() != null ? message.userId() : 0L,
                    List.of(item),
                    message.tipoMovimiento(),
                    message.movementId().toString());
            log.info("Orden de picking {} creada desde SALIDA {}.", orden.idOrden(), message.movementId());
            optimizarRuta.execute(orden.idOrden());
            log.info("Ruta optimizada para orden {}.", orden.idOrden());
        } catch (Exception e) {
            log.error("Error creando orden de picking desde SALIDA {}: {}", message.movementId(), e.getMessage());
        }
    }
}
