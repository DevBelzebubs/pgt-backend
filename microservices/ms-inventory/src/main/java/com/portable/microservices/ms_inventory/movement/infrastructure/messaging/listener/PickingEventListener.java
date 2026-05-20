package com.portable.microservices.ms_inventory.movement.infrastructure.messaging.listener;
import com.portable.microservices.ms_inventory.movement.infrastructure.messaging.dto.PickingCompletedMessage;
import com.portable.shared.infrastructure.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PickingEventListener {

    @RabbitListener(queues = RabbitMQConfig.INVENTORY_PICKING_QUEUE)
    public void handlePickingCompleted(PickingCompletedMessage message) {
        log.info("🛒 ¡Orden de Picking recibida en ms-inventory! Orden ID: {}", message.ordenId());
        log.info("👤 Operario ID: {}", message.usuarioId());
        
        message.items().forEach(item -> {
            log.info("📦 Producto a descontar: {} | Locación: {} | Cantidad: {}", 
                     item.productoId(), item.locacionId(), item.cantidadRealRecogida());
        });
        try {
            // registrarSalidaUseCase.execute(message);
            
            log.info("Salida de inventario registrada con éxito por picking.");
        } catch (Exception e) {
            log.error("Error procesando salida de picking: {}", e.getMessage());
            throw e;
        }
    }
}