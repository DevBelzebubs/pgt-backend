package com.portable.microservices.ms_inventory.locations.application.usecases.location;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.locations.domain.model.Location;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.location.GetLocationPortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.out.LocationPersistencePortOut;

@Service
public class GetLocationUseCase implements GetLocationPortIn {

    private final LocationPersistencePortOut locationPortOut;

    public GetLocationUseCase(LocationPersistencePortOut locationPortOut) {
        this.locationPortOut = locationPortOut;
    }

    @Override
    public Location execute(UUID idLocacion) {
        return locationPortOut.findById(idLocacion)
                .orElseThrow(() -> new IllegalStateException("Locación no encontrada: " + idLocacion));
    }
}
