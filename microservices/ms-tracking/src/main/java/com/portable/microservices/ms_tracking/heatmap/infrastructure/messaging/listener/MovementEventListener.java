package com.portable.microservices.ms_tracking.heatmap.infrastructure.messaging.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.portable.microservices.ms_tracking.heatmap.infrastructure.messaging.dto.MovementCreatedMessage;
import com.portable.microservices.ms_tracking.shared.infrastructure.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MovementEventListener {
    // private final UpdateHeatmapUseCase updateHeatmapUseCase;

    @RabbitListener(queues = RabbitMQConfig.TRACKING_QUEUE)
    public void handleMovementCreated(MovementCreatedMessage message) {
        log.info("Evento recibido en ms-tracking! Movimiento ID: {}, Tipo: {}, Locación: {}", 
                 message.movementId(), message.tipoMovimiento(), message.locacionId());
        
        try {
            // Aquí mapeas el mensaje a un Comando de tu dominio y llamas al Use Case
            // updateHeatmapUseCase.execute(command);
            
            log.info("Mapa de calor actualizado correctamente.");
        } catch (Exception e) {
            log.error("Error procesando el evento de movimiento: {}", e.getMessage());
            throw e; 
        }
    }
}