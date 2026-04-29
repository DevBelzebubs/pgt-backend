package com.portable.microservices.ms_administration.iam.domain.model;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

public record User (
    Long id,
    UUID uuid,
    String firstName,
    String lastName,
    String dni,
    Set<Role> roles,
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt
) {

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.name().equalsIgnoreCase(roleName));
    }
}
