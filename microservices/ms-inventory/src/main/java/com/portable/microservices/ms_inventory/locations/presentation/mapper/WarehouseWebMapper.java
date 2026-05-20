package com.portable.microservices.ms_inventory.locations.presentation.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.portable.microservices.ms_inventory.locations.domain.model.Warehouse;
import com.portable.microservices.ms_inventory.locations.presentation.dto.CreateWarehouseRequest;
import com.portable.microservices.ms_inventory.locations.presentation.dto.UpdateWarehouseRequest;
import com.portable.microservices.ms_inventory.locations.presentation.dto.WarehouseResponse;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WarehouseWebMapper {

    Warehouse toDomain(CreateWarehouseRequest request);

    Warehouse toDomain(UpdateWarehouseRequest request);

    WarehouseResponse toResponse(Warehouse warehouse);

    List<WarehouseResponse> toResponseList(List<Warehouse> warehouses);
}