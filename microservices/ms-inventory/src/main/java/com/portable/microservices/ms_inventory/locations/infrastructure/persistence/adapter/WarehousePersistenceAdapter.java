package com.portable.microservices.ms_inventory.locations.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;
import com.portable.microservices.ms_inventory.locations.domain.ports.out.WarehousePersistencePortOut;
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean existsById(Long id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Optional<Warehouse> findById(Long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public Warehouse save(Warehouse warehouse) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Warehouse> findAllActives() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllActives'");
    }


}
