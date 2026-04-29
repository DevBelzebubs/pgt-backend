package com.portable.microservices.ms_administration.iam.presentation.dto;

import java.util.UUID;

public record UserAccountResponse(
    UUID accountUuid,
    String username,
    String firstName,
    String lastName,
    String roleName,
    boolean active
) {}
