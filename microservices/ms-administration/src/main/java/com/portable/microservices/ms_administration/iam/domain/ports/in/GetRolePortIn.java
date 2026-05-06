package com.portable.microservices.ms_administration.iam.domain.ports.in;

import com.portable.microservices.ms_administration.iam.domain.model.Role;

public interface GetRolePortIn {
    Role execute(Long id);
}
