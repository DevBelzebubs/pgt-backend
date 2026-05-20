package com.portable.microservices.ms_inventory.locations.domain.ports.in.location;

import com.portable.microservices.ms_inventory.locations.domain.model.Location;

public interface CreateLocationPortIn {
    Location execute(Location location);
}
