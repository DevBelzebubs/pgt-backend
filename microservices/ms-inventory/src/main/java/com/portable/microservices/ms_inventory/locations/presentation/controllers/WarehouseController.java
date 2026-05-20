package com.portable.microservices.ms_inventory.locations.presentation.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portable.microservices.ms_inventory.locations.domain.ports.in.warehouse.CreateWarehousePortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.warehouse.DeleteWarehousePortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.warehouse.GetWarehousePortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.warehouse.ListWarehousePortIn;
import com.portable.microservices.ms_inventory.locations.domain.ports.in.warehouse.UpdateWarehousePortIn;
import com.portable.microservices.ms_inventory.locations.presentation.dto.CreateWarehouseRequest;
import com.portable.microservices.ms_inventory.locations.presentation.dto.UpdateWarehouseRequest;
import com.portable.microservices.ms_inventory.locations.presentation.dto.WarehouseResponse;
import com.portable.microservices.ms_inventory.locations.presentation.mapper.WarehouseWebMapper;
import com.portable.shared.infrastructure.presentation.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final CreateWarehousePortIn createWarehousePortIn;
    private final UpdateWarehousePortIn updateWarehousePortIn;
    private final GetWarehousePortIn getWarehousePortIn;
    private final ListWarehousePortIn listWarehousePortIn;
    private final DeleteWarehousePortIn deleteWarehousePortIn;
    private final WarehouseWebMapper mapper;

    @PostMapping
    public ResponseEntity<ApiResponse<WarehouseResponse>> create(@Valid @RequestBody CreateWarehouseRequest request) {
        var domain = mapper.toDomain(request);
        var created = createWarehousePortIn.execute(domain);
        var response = mapper.toResponse(created);
        return ResponseEntity.ok(ApiResponse.ok("Almacén creado exitosamente", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WarehouseResponse>> update(@PathVariable Long id, @RequestBody UpdateWarehouseRequest request) {
        var domain = mapper.toDomain(request);
        var updated = updateWarehousePortIn.execute(id, domain);
        var response = mapper.toResponse(updated);
        return ResponseEntity.ok(ApiResponse.ok("Almacén actualizado exitosamente", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WarehouseResponse>> getById(@PathVariable Long id) {
        var warehouse = getWarehousePortIn.execute(id);
        var response = mapper.toResponse(warehouse);
        return ResponseEntity.ok(ApiResponse.ok("Almacén encontrado", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WarehouseResponse>>> getAll() {
        var warehouses = listWarehousePortIn.execute();
        var responses = warehouses.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Lista de almacenes obtenida", responses));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        deleteWarehousePortIn.execute(id);
        return ResponseEntity.ok(ApiResponse.ok("Almacén eliminado exitosamente", null));
    }
}