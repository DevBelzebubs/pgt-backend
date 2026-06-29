package com.portable.microservices.ms_inventory.locations.application.usecases.location;

import java.util.List;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.locations.domain.model.Location;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.location.ListLocationsPortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.out.LocationPersistencePortOut;

@Component
public class ListLocationsUseCase implements ListLocationsPortIn {

    private final LocationPersistencePortOut locationPortOut;

    public ListLocationsUseCase(LocationPersistencePortOut locationPortOut) {
        this.locationPortOut = locationPortOut;
    }

    @Override
    public List<Location> execute() {
        return locationPortOut.findAllActives();
    }
}
