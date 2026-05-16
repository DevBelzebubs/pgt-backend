package com.portable.microservices.ms_inventory.locations.presentation.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portable.microservices.ms_inventory.locations.domain.ports.in.location.CreateLocationPortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.location.DeleteLocationPortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.location.GetLocationPortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.location.ListLocationsPortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.location.UpdateLocationPortIn;
import com.portable.microservices.ms_inventory.locations.presentation.dto.CreateLocationRequest;
import com.portable.microservices.ms_inventory.locations.presentation.dto.LocationResponse;
import com.portable.microservices.ms_inventory.locations.presentation.dto.UpdateLocationRequest;
import com.portable.microservices.ms_inventory.locations.presentation.mapper.LocationWebMapper;
import com.portable.shared.infrastructure.presentation.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationController {

    private final CreateLocationPortIn createLocationPortIn;
    private final UpdateLocationPortIn updateLocationPortIn;
    private final GetLocationPortIn getLocationPortIn;
    private final ListLocationsPortIn listLocationsPortIn;
    private final DeleteLocationPortIn deleteLocationPortIn;
    private final LocationWebMapper mapper;

   @PostMapping
    public ResponseEntity<ApiResponse<LocationResponse>> create(@Valid @RequestBody CreateLocationRequest request) {
        var domain = mapper.toDomain(request);
        var created = createLocationPortIn.execute(domain);
        var response = mapper.toResponse(created);
        return ResponseEntity.ok(ApiResponse.ok("Locación creada exitosamente", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LocationResponse>> update(@PathVariable UUID id, @RequestBody UpdateLocationRequest request) {
        var domain = mapper.toDomain(request);
        var updated = updateLocationPortIn.execute(id, domain);
        var response = mapper.toResponse(updated);
        return ResponseEntity.ok(ApiResponse.ok("Locación actualizada exitosamente", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LocationResponse>> getById(@PathVariable UUID id) {
        var location = getLocationPortIn.execute(id);
        var response = mapper.toResponse(location);
        return ResponseEntity.ok(ApiResponse.ok("Locación encontrada", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LocationResponse>>> getAll() {
        var locations = listLocationsPortIn.execute();
        var responses = locations.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Lista de locaciones obtenida", responses));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        deleteLocationPortIn.execute(id);
        // Enviamos null en la data, pero confirmamos el éxito con un mensaje
        return ResponseEntity.ok(ApiResponse.ok("Locación eliminada exitosamente", null));
    }
}