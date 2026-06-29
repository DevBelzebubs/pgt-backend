package com.portable.microservices.ms_inventory.product.domain.ports.in;

import java.util.UUID;

public interface DeleteProductPortIn {
    void delete(UUID id);
}
