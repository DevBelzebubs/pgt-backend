package com.portable.microservices.ms_administration.iam.application.usecases;

import java.util.List;

import com.portable.microservices.ms_administration.iam.domain.model.User;
import com.portable.microservices.ms_administration.iam.domain.ports.in.ListUsersPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListUsersUseCase implements ListUsersPortIn {
    private final UserPersistencePortOut userPersistence;

    @Override
    public List<User> execute() {
        return userPersistence.findAll();
    }
}
