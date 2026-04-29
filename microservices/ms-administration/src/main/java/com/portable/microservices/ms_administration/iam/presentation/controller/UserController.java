package com.portable.microservices.ms_administration.iam.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portable.microservices.ms_administration.iam.domain.ports.in.CreateUserPortIn;
import com.portable.microservices.ms_administration.iam.infrastructure.persistence.mapper.UserPresentationMapper;
import com.portable.microservices.ms_administration.iam.presentation.dto.CreateUserRequest;
import com.portable.microservices.ms_administration.iam.presentation.dto.UserAccountResponse;
import com.portable.shared.infrastructure.presentation.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final CreateUserPortIn createUserUseCase;
    private final UserPresentationMapper mapper;

    @PostMapping
    public ResponseEntity<ApiResponse<UserAccountResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
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

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Usuario creado exitosamente", response));
    }
}
