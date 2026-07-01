package com.portable.microservices.ms_administration.iam.infrastructure.messaging.dto;

import java.util.List;

public record GetUserNamesResponse(List<UserNameDto> users) {}