package com.portable.microservices.ms_inventory.shared.infrastructure.websocket;

import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.movement.domain.event.MovementCreatedEvent;
import com.portable.microservices.ms_inventory.shared.infrastructure.websocket.dto.MovementWsDto;
import com.portable.microservices.ms_inventory.shared.infrastructure.websocket.dto.StockAlertWsDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventPublisher {
    private final SimpMessagingTemplate messagingTemplate;
    public void publishMovementCreated(MovementCreatedEvent event) {
        MovementWsDto dto = new MovementWsDto(
            event.movementId(),
            event.productId(),
            event.tipoMovimiento(),
            event.cantidad()
        );
        messagingTemplate.convertAndSend("/topic/inventory/movements", dto);
    }
    public void publishStockAlert(UUID productId, Integer currentStock, Integer minStock) {
        StockAlertWsDto dto = new StockAlertWsDto(productId, currentStock, minStock);
        messagingTemplate.convertAndSend("/topic/inventory/stock-alerts", dto);
    }
}