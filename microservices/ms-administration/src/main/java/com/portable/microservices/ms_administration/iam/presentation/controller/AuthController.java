package com.portable.microservices.ms_administration.iam.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portable.microservices.ms_administration.iam.domain.ports.in.LoginPortIn;
import com.portable.microservices.ms_administration.iam.presentation.dto.LoginRequest;
import com.portable.microservices.ms_administration.iam.presentation.dto.LoginResponse;
import com.portable.shared.infrastructure.presentation.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginPortIn loginUseCase;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        String token = loginUseCase.execute(request.username(), request.password());

        LoginResponse responseData = new LoginResponse(token);

        return ResponseEntity.ok(
                ApiResponse.ok("Login exitoso", responseData));
    }
}
