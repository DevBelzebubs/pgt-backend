package com.portable.microservices.ms_administration.iam.domain.ports.in;

import java.util.UUID;

public interface DeleteUserPortIn {
    void execute(UUID uuid);
}
