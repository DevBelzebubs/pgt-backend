package com.portable.microservices.ms_inventory.locations.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;
import com.portable.microservices.ms_inventory.locations.domain.ports.out.WarehousePersistencePortOut;
import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.entity.WarehouseJpaEntity;
import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.mapper.WarehousePersistenceMapper;
import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.repository.WarehouseJpaRepository;

@Component
public class WarehousePersistenceAdapter implements WarehousePersistencePortOut {


    private final WarehouseJpaRepository repository;
    private final WarehousePersistenceMapper mapper;

    public WarehousePersistenceAdapter(WarehouseJpaRepository repository,
        WarehousePersistenceMapper mapper
        
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public boolean existsByCodAlm(String codAlm) {
        return repository.existsByCodAlm(codAlm);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<Warehouse> findById(Long id) {
       return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Warehouse save(Warehouse warehouse) {
        // 1. Convertimos el objeto de Dominio a Entidad de BD
        WarehouseJpaEntity entity = mapper.toEntity(warehouse);
        
        // 2. Lo guardamos en la tabla
        WarehouseJpaEntity savedEntity = repository.save(entity);
        
        // 3. Convertimos la Entidad guardada de regreso a Dominio para el Caso de Uso
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<Warehouse> findAllActives() {
return repository.findByActivoTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }


}
