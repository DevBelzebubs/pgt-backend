package com.portable.microservices.ms_inventory.locations.infrastructure.persistence.mapper;


import org.mapstruct.Mapper;

import com.portable.microservices.ms_inventory.locations.domain.model.Location;
import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.entity.LocationJpaEntity;

@Mapper(componentModel = "spring")
public interface LocationPersistenceMapper {

    Location toDomain(LocationJpaEntity locationJpaEntity);
    LocationJpaEntity toEntity(Location domain);

}
