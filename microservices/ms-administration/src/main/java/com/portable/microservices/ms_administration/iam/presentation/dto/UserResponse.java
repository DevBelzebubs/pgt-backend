package com.portable.microservices.ms_administration.iam.presentation.dto;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
    UUID uuid,
    String firstName,
    String lastName,
    String dni,
    Set<String> roles,
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt
) {}
