package com.portable.microservices.ms_inventory.alert.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class StockAlert {
    private final UUID id;
    private final UUID productId;
    private final Integer currentStock;
    private final Integer minStock;
    private String status;
    private final LocalDateTime createdAt;

    public StockAlert(UUID productId, Integer currentStock, Integer minStock) {
        this.id = UUID.randomUUID();
        this.productId = productId;
        this.currentStock = currentStock;
        this.minStock = minStock;
        this.status = "ACTIVA";
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public Integer getMinStock() {
        return minStock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
}