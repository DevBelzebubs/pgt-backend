package com.portable.microservices.ms_inventory.movement.infrastructure.messaging.listener;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.RegisterEntradaPortIn;
import com.portable.microservices.ms_inventory.movement.infrastructure.config.RabbitMQConfig;
import com.portable.microservices.ms_inventory.movement.infrastructure.messaging.dto.PickingCompletedMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PickingEventListener {

    private final RegisterEntradaPortIn registerEntradaPortIn;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConfig.INVENTORY_PICKING_QUEUE, durable = "true"),
            exchange = @Exchange(value = RabbitMQConfig.TRACKING_EXCHANGE, type = "topic"),
            key = RabbitMQConfig.ROUTING_KEY
    ))
    public void handlePickingCompleted(PickingCompletedMessage message) {
        log.info("🛒 ¡Orden de Picking recibida en ms-inventory! Orden ID: {}", message.ordenId());
        log.info("👤 Operario ID: {}", message.usuarioId());
        
        message.items().forEach(item -> {
            log.info("📦 Producto a descontar: {} | Locación: {} | Cantidad: {}", 
                     item.productoId(), item.locacionId(), item.cantidadRealRecogida());
        });
        try {
            // Nota: Este listener registra SALIDA (descuento) de picking
            // Para ahora solo log. En futuro se conectará con RegisterSalidaUseCase
            
            log.info("Evento de picking procesado exitosamente.");
        } catch (Exception e) {
            log.error("Error procesando evento de picking: {}", e.getMessage());
            throw e;
        }
    }
}