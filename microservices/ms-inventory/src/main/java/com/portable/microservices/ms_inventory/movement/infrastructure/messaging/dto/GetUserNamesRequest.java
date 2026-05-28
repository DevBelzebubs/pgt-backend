package com.portable.microservices.ms_inventory.movement.infrastructure.messaging.dto;

import java.util.List;

public record GetUserNamesRequest(List<Long> userIds) {}
