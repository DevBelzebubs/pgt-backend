package com.portable.microservices.ms_inventory.locations.presentation.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.portable.microservices.ms_inventory.locations.domain.model.Location;
import com.portable.microservices.ms_inventory.locations.presentation.dto.CreateLocationRequest;
import com.portable.microservices.ms_inventory.locations.presentation.dto.LocationResponse;
import com.portable.microservices.ms_inventory.locations.presentation.dto.UpdateLocationRequest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationWebMapper {

    Location toDomain(CreateLocationRequest request);

    Location toDomain(UpdateLocationRequest request);

    LocationResponse toResponse(Location location);

    List<LocationResponse> toResponseList(List<Location> locations);
}