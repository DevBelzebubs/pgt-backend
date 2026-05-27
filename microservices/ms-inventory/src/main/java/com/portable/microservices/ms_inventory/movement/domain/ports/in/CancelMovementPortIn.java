package com.portable.microservices.ms_inventory.movement.domain.ports.in;

import java.util.UUID;

public interface CancelMovementPortIn {
    void execute(UUID id);
}
