package com.portable.microservices.ms_inventory.locations.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;
import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.entity.WarehouseJpaEntity;

@Mapper(componentModel = "spring")
public interface WarehousePersistenceMapper {

    Warehouse toDomain(WarehouseJpaEntity warehouseJpaEntity);
    WarehouseJpaEntity toEntity(Warehouse domain);

}
