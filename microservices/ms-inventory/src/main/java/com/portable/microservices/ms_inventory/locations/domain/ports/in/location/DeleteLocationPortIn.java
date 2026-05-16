package com.portable.microservices.ms_inventory.locations.domain.ports.in.location;

import java.util.UUID;

public interface DeleteLocationPortIn {
    void execute(UUID idLocacion);
}
