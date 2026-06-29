package com.portable.microservices.ms_administration.iam.domain.ports.in;

import com.portable.microservices.ms_administration.iam.domain.model.Role;

public interface UpdateRolePortIn {
    Role execute(Long id, String newName);
}
