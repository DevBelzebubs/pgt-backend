package com.portable.microservices.ms_inventory.locations.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.locations.domain.model.Location;
import com.portable.microservices.ms_inventory.locations.domain.ports.out.LocationPersistencePortOut;
import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.entity.LocationJpaEntity;
import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.mapper.LocationPersistenceMapper;
import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.repository.LocationJpaRepository;

@Component
public class LocationPersistenceAdapter implements LocationPersistencePortOut {

    private final LocationJpaRepository repository;

    private final LocationPersistenceMapper mapper;

    public LocationPersistenceAdapter(LocationJpaRepository repository, LocationPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public boolean existsByCodBarras(String codBarras) {
        return repository.existsByCodBarras(codBarras);
    }

    @Override
    public List<Location> findAllActives() {
        return repository.findByActivoTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Location> findById(UUID idLocacion) {
        return repository.findById(idLocacion).map(mapper::toDomain);
    }

    @Override
    public Location save(Location location) {
        // 1. Dominio -> Entidad (Idioma BD)
        LocationJpaEntity entity = mapper.toEntity(location);
        // 2. Guardamos en BD
        LocationJpaEntity savedEntity = repository.save(entity);
        // 3. Entidad guardada -> Dominio de regreso al Caso de Uso
        return mapper.toDomain(savedEntity);
    }


}
