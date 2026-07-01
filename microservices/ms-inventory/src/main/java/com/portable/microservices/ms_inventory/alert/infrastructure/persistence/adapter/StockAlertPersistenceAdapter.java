package com.portable.microservices.ms_inventory.alert.infrastructure.persistence.adapter;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.alert.domain.model.StockAlert;
import com.portable.microservices.ms_inventory.alert.domain.ports.out.StockAlertPersistencePort;
import com.portable.microservices.ms_inventory.alert.infrastructure.persistence.repository.AlertJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StockAlertPersistenceAdapter implements StockAlertPersistencePort {

    private final AlertJpaRepository alertRepository;

    @Override
    public void save(StockAlert alert) {
        /*AlertJpaEntity entity = new AlertJpaEntity();
        entity.setId(alert.getId());
        entity.setProductId(alert.getProductId());
        entity.setCurrentStock(alert.getCurrentStock());
        entity.setMinStock(alert.getMinStock());
        entity.setStatus(alert.getStatus());
        
        alertRepository.save(entity);*/
    }

    @Override
    public boolean hasActiveAlertForProduct(UUID productId) {
        return alertRepository.existsByProductIdAndStatus(productId, "ACTIVA");
    }
}