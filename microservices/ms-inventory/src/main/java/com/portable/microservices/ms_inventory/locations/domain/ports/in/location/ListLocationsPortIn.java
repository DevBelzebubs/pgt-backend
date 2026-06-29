package com.portable.microservices.ms_inventory.locations.domain.ports.in.location;

import java.util.List;

import com.portable.microservices.ms_inventory.locations.domain.model.Location;

public interface ListLocationsPortIn {
 List<Location> execute();
}
