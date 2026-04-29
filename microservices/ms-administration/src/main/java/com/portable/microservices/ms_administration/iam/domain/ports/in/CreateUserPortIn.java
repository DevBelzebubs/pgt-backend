package com.portable.microservices.ms_administration.iam.domain.ports.in;

import com.portable.microservices.ms_administration.iam.domain.model.Account;

public interface CreateUserPortIn {
    Account execute(CreateUserCommand command);

    record CreateUserCommand(
        String firstName,
        String lastName,
        String dni,
        String username,
        String plainPassword,
        Long headquarterId,
        String roleName
    ) {}
}