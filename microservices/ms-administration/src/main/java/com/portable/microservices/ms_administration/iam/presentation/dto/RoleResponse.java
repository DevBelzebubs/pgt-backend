package com.portable.microservices.ms_administration.iam.presentation.dto;

import java.time.ZonedDateTime;

public record RoleResponse(
    Long id,
    String name,
    ZonedDateTime createdAt
) {}
