package com.portable.microservices.ms_inventory.locations.domain.ports.in.location;

import java.util.UUID;

import com.portable.microservices.ms_inventory.locations.domain.model.Location;

public interface UpdateLocationPortIn {
    Location execute(UUID idLocacion, Location location);
}
