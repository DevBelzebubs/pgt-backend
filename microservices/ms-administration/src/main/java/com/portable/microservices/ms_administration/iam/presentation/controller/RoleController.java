package com.portable.microservices.ms_administration.iam.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.ports.in.CreateRolePortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.DeleteRolePortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.GetRolePortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.ListRolesPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.UpdateRolePortIn;
import com.portable.microservices.ms_administration.iam.presentation.dto.CreateRoleRequest;
import com.portable.microservices.ms_administration.iam.presentation.dto.RoleResponse;
import com.portable.shared.infrastructure.presentation.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final CreateRolePortIn createRoleUseCase;
    private final ListRolesPortIn listRolesUseCase;
    private final GetRolePortIn getRoleUseCase;
    private final UpdateRolePortIn updateRoleUseCase;
    private final DeleteRolePortIn deleteRoleUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(@Valid @RequestBody CreateRoleRequest request) {
        Role saved = createRoleUseCase.execute(request.name());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Rol creado", toResponse(saved)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> list() {
        List<RoleResponse> roles = listRolesUseCase.execute().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Roles listados", roles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> get(@PathVariable Long id) {
        Role role = getRoleUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.ok("Rol encontrado", toResponse(role)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> update(@PathVariable Long id,
                                                             @Valid @RequestBody CreateRoleRequest request) {
        Role updated = updateRoleUseCase.execute(id, request.name());
        return ResponseEntity.ok(ApiResponse.ok("Rol actualizado", toResponse(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        deleteRoleUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.ok("Rol eliminado", null));
    }

    private RoleResponse toResponse(Role role) {
        return new RoleResponse(role.id(), role.name(), role.createdAt());
    }
}
