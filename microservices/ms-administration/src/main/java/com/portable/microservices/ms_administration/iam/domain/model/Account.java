package com.portable.microservices.ms_administration.iam.domain.model;

import java.time.ZonedDateTime;
import java.util.UUID;

public record Account(
    Long id,
    UUID uuid,
    User user,
    Long userId,
    Long headquarterId,
    String username,
    String password,
    boolean active,
    ZonedDateTime createdAt
) {

    public boolean canLogin() {
        return active;
    }
}
