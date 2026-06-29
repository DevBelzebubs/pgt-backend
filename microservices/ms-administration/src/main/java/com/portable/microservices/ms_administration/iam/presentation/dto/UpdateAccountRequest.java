package com.portable.microservices.ms_administration.iam.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateAccountRequest(
    @NotNull Long headquarterId,
    boolean active
) {}
