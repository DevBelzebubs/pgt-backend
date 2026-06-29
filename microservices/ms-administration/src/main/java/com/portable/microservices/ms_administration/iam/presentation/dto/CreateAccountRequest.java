package com.portable.microservices.ms_administration.iam.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(
    @NotNull Long userId,
    @NotNull Long headquarterId,
    @NotBlank @Size(min = 4, max = 50) String username,
    @NotBlank @Size(min = 8) String password,
    boolean active
) {}
