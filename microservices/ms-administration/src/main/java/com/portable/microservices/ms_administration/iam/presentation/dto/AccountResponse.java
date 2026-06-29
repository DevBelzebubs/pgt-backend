package com.portable.microservices.ms_administration.iam.presentation.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record AccountResponse(
    UUID uuid,
    String username,
    String firstName,
    String lastName,
    boolean active,
    Long headquarterId,
    ZonedDateTime createdAt
) {}
