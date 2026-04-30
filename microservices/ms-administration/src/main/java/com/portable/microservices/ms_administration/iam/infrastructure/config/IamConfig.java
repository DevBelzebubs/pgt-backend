package com.portable.microservices.ms_administration.iam.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.portable.microservices.ms_administration.iam.application.usecases.CreateUserUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.DeleteUserUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.GetUserUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.ListUsersUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.UpdateUserUseCase;
import com.portable.microservices.ms_administration.iam.domain.ports.in.CreateUserPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.PasswordEncryptationPortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;

@Configuration
public class IamConfig {
    
    @Bean
    public CreateUserPortIn createUserPortIn(UserPersistencePortOut userPersistence, AccountPersistencePortOut accountPersistence, RolePersistencePortOut rolePersistence, PasswordEncryptationPortOut passwordEncryptation) {
        return new CreateUserUseCase(userPersistence, accountPersistence, rolePersistence, passwordEncryptation);
    }

    @Bean
    public ListUsersUseCase listUsersPortIn(UserPersistencePortOut userPersistence) {
        return new ListUsersUseCase(userPersistence);
    }

    @Bean
    public GetUserUseCase getUserPortIn(UserPersistencePortOut userPersistence) {
        return new GetUserUseCase(userPersistence);
    }

    @Bean
    public UpdateUserUseCase updateUserPortIn(UserPersistencePortOut userPersistence, RolePersistencePortOut rolePersistence) {
        return new UpdateUserUseCase(userPersistence, rolePersistence);
    }

    @Bean
    public DeleteUserUseCase deleteUserPortIn(UserPersistencePortOut userPersistence) {
        return new DeleteUserUseCase(userPersistence);
    }
}
