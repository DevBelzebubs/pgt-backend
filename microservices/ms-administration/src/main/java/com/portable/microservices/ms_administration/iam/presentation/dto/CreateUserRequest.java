package com.portable.microservices.ms_administration.iam.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank @Size(max = 80) String firstName,
    @NotBlank @Size(max = 80) String lastName,
    @NotBlank @Pattern(regexp = "\\d{8}") String dni,
    @NotBlank @Size(min = 4, max = 50) String username,
    @NotBlank @Size(min = 8) String password,
    @NotNull Long headquarterId,
    @NotBlank String roleName
) {}
