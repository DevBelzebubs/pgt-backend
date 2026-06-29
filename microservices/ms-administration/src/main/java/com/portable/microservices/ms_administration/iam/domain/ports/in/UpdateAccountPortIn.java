package com.portable.microservices.ms_administration.iam.domain.ports.in;

import java.util.UUID;

import com.portable.microservices.ms_administration.iam.domain.model.Account;

public interface UpdateAccountPortIn {
    Account execute(UpdateAccountCommand command);

    record UpdateAccountCommand(
        UUID uuid,
        Long headquarterId,
        boolean active
    ) {}
}
