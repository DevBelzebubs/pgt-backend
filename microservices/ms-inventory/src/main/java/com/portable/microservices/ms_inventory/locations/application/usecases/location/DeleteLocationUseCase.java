package com.portable.microservices.ms_inventory.locations.application.usecases.location;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.locations.domain.model.Location;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.location.DeleteLocationPortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.out.LocationPersistencePortOut;

@Service
public class DeleteLocationUseCase implements DeleteLocationPortIn {

    private final LocationPersistencePortOut persistence;

    public DeleteLocationUseCase(LocationPersistencePortOut persistence) {
        this.persistence = persistence;
    }

    @Override
    public void execute(UUID idLocacion) {
        Location existing = persistence.findById(idLocacion)
            .orElseThrow(() -> new RuntimeException("Locación no encontrada"));
            
        // Simplemente clonamos y apagamos el activo
        Location disabled = existing.toBuilder()
            .activo(false)
            .build();
            
        persistence.save(disabled);
    }
}
