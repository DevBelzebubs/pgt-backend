package com.portable.microservices.ms_inventory.shared.domain.event;

import java.util.UUID;

public record StockDecreasedEvent(UUID productId, Integer newStock) {}