package com.portable.microservices.ms_inventory.alert.domain.ports.out;

import java.util.UUID;


import com.portable.microservices.ms_inventory.alert.domain.model.StockAlert;

public interface StockAlertPersistencePort {
    void save(StockAlert alert);
    boolean hasActiveAlertForProduct(UUID productId);
}