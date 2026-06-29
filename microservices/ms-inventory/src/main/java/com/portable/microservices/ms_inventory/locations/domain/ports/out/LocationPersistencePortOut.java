package com.portable.microservices.ms_inventory.locations.domain.ports.out;

import com.portable.microservices.ms_inventory.locations.domain.model.Location;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationPersistencePortOut {
    Location save(Location location);
    Optional<Location> findById(UUID idLocacion);
    List<Location> findAllActives();
    boolean existsByCodBarras(String codBarras);
    // Nota: Como estamos haciendo el CRUD de Almacén, ya no necesitamos 
    // el truco de la consulta nativa aquí. El UseCase de Locación 
    // llamará directamente al puerto del Almacén para validar.
}
