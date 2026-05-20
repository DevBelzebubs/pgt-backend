package com.portable.microservices.ms_inventory.alert.infrastructure.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.portable.microservices.ms_inventory.alert.application.usecases.GenerateStockAlertUseCase;
import com.portable.microservices.ms_inventory.shared.domain.event.StockDecreasedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockDecreaseEventListener {
    private final GenerateStockAlertUseCase generateStockAlertUseCase;
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleStockDecreased(StockDecreasedEvent event) {
        log.info("Evaluando niveles de stock para producto: {}", event.productId());
        generateStockAlertUseCase.execute(event.productId(), event.newStock());
    }
}