package com.portable.microservices.ms_administration.iam.domain.ports.in;

import java.util.List;

import com.portable.microservices.ms_administration.iam.domain.model.Role;

public interface ListRolesPortIn {
    List<Role> execute();
}
