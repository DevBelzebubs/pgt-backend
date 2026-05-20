package com.portable.microservices.ms_inventory.alert.domain.ports.out;

import java.util.UUID;

public interface ProductThresholdPort {
    Integer getMinStockThreshold(UUID productId);
}
