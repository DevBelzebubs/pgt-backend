package com.portable.microservices.ms_administration.iam.presentation.controller;

import java.util.List;
import java.util.UUID;

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

import com.portable.microservices.ms_administration.iam.domain.ports.in.CreateUserPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.DeleteUserPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.GetUserPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.ListUsersPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.UpdateUserPortIn;
import com.portable.microservices.ms_administration.iam.infrastructure.persistence.mapper.UserPresentationMapper;
import com.portable.microservices.ms_administration.iam.presentation.dto.CreateUserRequest;
import com.portable.microservices.ms_administration.iam.presentation.dto.UpdateUserRequest;
import com.portable.microservices.ms_administration.iam.presentation.dto.UserAccountResponse;
import com.portable.microservices.ms_administration.iam.presentation.dto.UserResponse;
import com.portable.shared.infrastructure.presentation.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserPortIn createUserUseCase;
    private final ListUsersPortIn listUsersUseCase;
    private final GetUserPortIn getUserUseCase;
    private final UpdateUserPortIn updateUserUseCase;
    private final DeleteUserPortIn deleteUserUseCase;
    private final UserPresentationMapper mapper;


    @PostMapping
    public ResponseEntity<ApiResponse<UserAccountResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        var command = new CreateUserPortIn.CreateUserCommand(
                request.firstName(),
                request.lastName(),
                request.dni(),
                request.username(),
                request.password(),
                request.headquarterId(),
                request.roleName()
        );

        var account = createUserUseCase.execute(command);
        var response = mapper.toResponse(account);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Usuario registrado exitosamente", response));
    }

    /** GET /api/v1/users — Requires JWT. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> listUsers() {
        var responses = listUsersUseCase.execute().stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Usuarios listados", responses));
    }

    /** GET /api/v1/users/{uuid} — Requires JWT. */
    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable UUID uuid) {
        var user = getUserUseCase.execute(uuid);
        return ResponseEntity.ok(ApiResponse.ok("Usuario encontrado", mapper.toResponse(user)));
    }

    /** PUT /api/v1/users/{uuid} — Requires JWT. */
    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID uuid,
            @Valid @RequestBody UpdateUserRequest request) {

        var command = new UpdateUserPortIn.UpdateUserCommand(
                uuid,
                request.firstName(),
                request.lastName(),
                request.headquarterId(),
                request.roleName()
        );

        var updated = updateUserUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ok("Usuario actualizado", mapper.toResponse(updated)));
    }


    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID uuid) {
        deleteUserUseCase.execute(uuid);
        return ResponseEntity.ok(ApiResponse.ok("Usuario eliminado", null));
    }
}
