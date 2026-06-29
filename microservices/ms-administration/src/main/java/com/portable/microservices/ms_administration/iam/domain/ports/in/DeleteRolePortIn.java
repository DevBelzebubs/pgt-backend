package com.portable.microservices.ms_administration.iam.domain.ports.in;

public interface DeleteRolePortIn {
    void execute(Long id);
}
