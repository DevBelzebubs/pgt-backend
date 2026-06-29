package com.portable.microservices.ms_inventory.locations.presentation.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;
import com.portable.microservices.ms_inventory.locations.presentation.dto.CreateWarehouseRequest;
import com.portable.microservices.ms_inventory.locations.presentation.dto.UpdateWarehouseRequest;
import com.portable.microservices.ms_inventory.locations.presentation.dto.WarehouseResponse;

@Component
public class WarehouseWebMapper {

    public Warehouse toDomain(CreateWarehouseRequest request) {
        if (request == null) return null;
        return Warehouse.builder()
                .idSede(request.idSede())
                .codAlm(request.codAlm())
                .nombre(request.nombre())
                .tipo(request.tipo())
                .build();
    }

    public Warehouse toDomain(UpdateWarehouseRequest request) {
        if (request == null) return null;
        return Warehouse.builder()
                .codAlm(request.codAlm())
                .nombre(request.nombre())
                .tipo(request.tipo())
                .build();
    }

    public WarehouseResponse toResponse(Warehouse warehouse) {
        if (warehouse == null) return null;
        return new WarehouseResponse(
                warehouse.id(),
                warehouse.idSede(),
                warehouse.almacenUuid(),
                warehouse.codAlm(),
                warehouse.nombre(),
                warehouse.tipo(),
                warehouse.activo()
        );
    }

    public List<WarehouseResponse> toResponseList(List<Warehouse> warehouses) {
        if (warehouses == null) return null;
        return warehouses.stream()
                .map(this::toResponse)
                .toList();
    }
}