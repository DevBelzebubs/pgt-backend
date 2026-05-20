package com.portable.microservices.ms_inventory.alert.application.usecases;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.alert.domain.model.StockAlert;
import com.portable.microservices.ms_inventory.alert.domain.ports.out.ProductThresholdPort;
import com.portable.microservices.ms_inventory.alert.domain.ports.out.StockAlertPersistencePort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenerateStockAlertUseCase {

    private final StockAlertPersistencePort alertRepository;
    private final ProductThresholdPort productThresholdPort;

    public void execute(UUID productId, Integer currentStock) {
        Integer minStock = productThresholdPort.getMinStockThreshold(productId);

        if (currentStock <= minStock) {
            if (!alertRepository.hasActiveAlertForProduct(productId)) {
                StockAlert alert = new StockAlert(productId, currentStock, minStock);
                alertRepository.save(alert);
                // Opcional: Aquí podrías inyectar otro puerto para enviar un WebSocket al Frontend
            }
        } else {
            // alertRepository.resolveActiveAlerts(productId);
        }
    }
}