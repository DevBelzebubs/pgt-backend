package com.portable.microservices.ms_inventory.movement.infrastructure.messaging.listener;

import java.util.List;
import com.portable.microservices.ms_inventory.lot.infrastructure.persistence.entity.LoteJpaEntity;
import com.portable.microservices.ms_inventory.movement.domain.ports.in.RegisterSalidaPortIn;
import com.portable.microservices.ms_inventory.movement.domain.ports.out.LotePersistencePortOut;
import com.portable.microservices.ms_inventory.movement.infrastructure.messaging.dto.PickingCompletedMessage;
import com.portable.shared.infrastructure.config.RabbitMQConfig;

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

    private final RegisterSalidaPortIn registerSalidaPortIn;
    private final LotePersistencePortOut lotePersistence;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConfig.INVENTORY_PICKING_QUEUE, durable = "true"),
            exchange = @Exchange(value = RabbitMQConfig.TRACKING_EXCHANGE, type = "topic"),
            key = RabbitMQConfig.ROUTING_KEY
    ))
    public void handlePickingCompleted(PickingCompletedMessage message) {
        log.info("🛒 ¡Orden de Picking recibida en ms-inventory! Orden ID: {}", message.ordenId());
        log.info("👤 Operario ID: {}", message.usuarioId());
        
        try {
            for (PickingCompletedMessage.ItemRecogido item : message.items()) {
                log.info("📦 Producto a descontar: {} | Locación: {} | Cantidad: {}", 
                         item.productoId(), item.locacionId(), item.cantidadRealRecogida());
                
                // Buscar lote correspondiente en esa locación usando la estrategia FIFO
                List<LoteJpaEntity> lotes = lotePersistence.findLotesByProductAndLocation(item.productoId(), item.locacionId());
                if (lotes.isEmpty()) {
                    throw new IllegalArgumentException(String.format(
                            "No se encontró un lote disponible para el producto: %s en la locación: %s", 
                            item.productoId(), item.locacionId()));
                }
                
                // FIFO: seleccionamos el primer lote de la lista ordenada por fecha de ingreso ascendente
                LoteJpaEntity lote = lotes.get(0);
                log.info("Lote seleccionado para picking (FIFO): {} | Nro Lote: {} | Fec Ingreso: {}", 
                         lote.getIdLote(), lote.getNroLote(), lote.getFecIngreso());

                registerSalidaPortIn.execute(
                        lote.getIdLote(),
                        message.usuarioId(),
                        item.cantidadRealRecogida(),
                        "Salida por Picking - Orden ID: " + message.ordenId(),
                        message.ordenId().toString()
                );
            }
            
            log.info("Evento de picking procesado exitosamente.");
        } catch (Exception e) {
            log.error("Error procesando evento de picking: {}", e.getMessage());
            throw e;
        }
    }
}