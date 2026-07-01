package com.portable.microservices.ms_administration.iam.infrastructure.messaging.dto;

import java.util.List;

public record GetUserNamesRequest(List<Long> userIds) {}
