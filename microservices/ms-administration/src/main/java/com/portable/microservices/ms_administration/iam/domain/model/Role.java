package com.portable.microservices.ms_administration.iam.domain.model;

import java.time.ZonedDateTime;

public record Role(
    Long id,
    String name,
    ZonedDateTime createdAt
) {}
