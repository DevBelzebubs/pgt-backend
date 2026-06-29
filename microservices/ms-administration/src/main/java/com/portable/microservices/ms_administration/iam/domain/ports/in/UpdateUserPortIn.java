package com.portable.microservices.ms_administration.iam.domain.ports.in;

import java.util.UUID;

import com.portable.microservices.ms_administration.iam.domain.model.User;

public interface UpdateUserPortIn {
    User execute(UpdateUserCommand command);

    record UpdateUserCommand(
        UUID uuid,
        String firstName,
        String lastName,
        Long headquarterId,
        String roleName
    ) {}
}
