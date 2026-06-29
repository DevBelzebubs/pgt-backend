package com.portable.microservices.ms_administration.iam.domain.ports.in;

import java.util.UUID;

import com.portable.microservices.ms_administration.iam.domain.model.User;

public interface GetUserPortIn {
    User execute(UUID uuid);
}
