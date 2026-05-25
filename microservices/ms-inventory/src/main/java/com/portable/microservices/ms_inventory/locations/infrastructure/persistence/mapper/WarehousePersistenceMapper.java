package com.portable.microservices.ms_inventory.locations.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;
import com.portable.microservices.ms_inventory.locations.infrastructure.persistence.entity.WarehouseJpaEntity;

@Component
public class WarehousePersistenceMapper {

    public Warehouse toDomain(WarehouseJpaEntity entity) {
        if (entity == null) return null;
        return Warehouse.builder()
                .id(entity.getId())
                .idSede(entity.getIdSede())
                .almacenUuid(entity.getAlmacenUuid())
                .codAlm(entity.getCodAlm())
                .nombre(entity.getNombre())
                .tipo(entity.getTipo())
                .activo(entity.getActivo())
                .build();
    }

    public WarehouseJpaEntity toEntity(Warehouse domain) {
        if (domain == null) return null;
        WarehouseJpaEntity entity = new WarehouseJpaEntity();
        entity.setId(domain.id());
        entity.setIdSede(domain.idSede());
        entity.setAlmacenUuid(domain.almacenUuid());
        entity.setCodAlm(domain.codAlm());
        entity.setNombre(domain.nombre());
        entity.setTipo(domain.tipo());
        entity.setActivo(domain.activo());
        return entity;
    }
}
