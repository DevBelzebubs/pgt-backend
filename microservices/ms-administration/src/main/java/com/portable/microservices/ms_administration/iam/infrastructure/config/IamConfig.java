package com.portable.microservices.ms_administration.iam.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.portable.microservices.ms_administration.iam.application.usecases.CreateRoleUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.CreateUserUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.DeleteAccountUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.DeleteRoleUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.DeleteUserUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.GetAccountUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.GetRoleUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.GetUserUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.ListAccountsUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.ListRolesUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.ListUsersUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.UpdateAccountUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.UpdateRoleUseCase;
import com.portable.microservices.ms_administration.iam.application.usecases.UpdateUserUseCase;
import com.portable.microservices.ms_administration.iam.domain.ports.in.CreateRolePortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.CreateUserPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.DeleteAccountPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.DeleteRolePortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.DeleteUserPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.GetAccountPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.GetRolePortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.GetUserPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.ListAccountsPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.ListRolesPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.ListUsersPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.UpdateAccountPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.UpdateRolePortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.UpdateUserPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.PasswordEncryptationPortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;

@Configuration
public class IamConfig {

    // ── USER ──────────────────────────────────────────────────────────────────

    @Bean
    public CreateUserPortIn createUserPortIn(UserPersistencePortOut userPersistence,
                                              AccountPersistencePortOut accountPersistence,
                                              RolePersistencePortOut rolePersistence,
                                              PasswordEncryptationPortOut passwordEncryptation) {
        return new CreateUserUseCase(userPersistence, accountPersistence, rolePersistence, passwordEncryptation);
    }

    @Bean
    public ListUsersPortIn listUsersPortIn(UserPersistencePortOut userPersistence) {
        return new ListUsersUseCase(userPersistence);
    }

    @Bean
    public GetUserPortIn getUserPortIn(UserPersistencePortOut userPersistence) {
        return new GetUserUseCase(userPersistence);
    }

    @Bean
    public UpdateUserPortIn updateUserPortIn(UserPersistencePortOut userPersistence,
                                              RolePersistencePortOut rolePersistence,
                                              AccountPersistencePortOut accountPersistence) {
        return new UpdateUserUseCase(userPersistence, rolePersistence, accountPersistence);
    }

    @Bean
    public DeleteUserPortIn deleteUserPortIn(UserPersistencePortOut userPersistence,
                                              AccountPersistencePortOut accountPersistence) {
        return new DeleteUserUseCase(userPersistence, accountPersistence);
    }

    // ── ROLE ──────────────────────────────────────────────────────────────────

    @Bean
    public CreateRolePortIn createRolePortIn(RolePersistencePortOut rolePersistence) {
        return new CreateRoleUseCase(rolePersistence);
    }

    @Bean
    public ListRolesPortIn listRolesPortIn(RolePersistencePortOut rolePersistence) {
        return new ListRolesUseCase(rolePersistence);
    }

    @Bean
    public GetRolePortIn getRolePortIn(RolePersistencePortOut rolePersistence) {
        return new GetRoleUseCase(rolePersistence);
    }

    @Bean
    public UpdateRolePortIn updateRolePortIn(RolePersistencePortOut rolePersistence) {
        return new UpdateRoleUseCase(rolePersistence);
    }

    @Bean
    public DeleteRolePortIn deleteRolePortIn(RolePersistencePortOut rolePersistence) {
        return new DeleteRoleUseCase(rolePersistence);
    }

    // ── ACCOUNT ───────────────────────────────────────────────────────────────

    @Bean
    public GetAccountPortIn getAccountPortIn(AccountPersistencePortOut accountPersistence) {
        return new GetAccountUseCase(accountPersistence);
    }

    @Bean
    public ListAccountsPortIn listAccountsPortIn(AccountPersistencePortOut accountPersistence) {
        return new ListAccountsUseCase(accountPersistence);
    }

    @Bean
    public UpdateAccountPortIn updateAccountPortIn(AccountPersistencePortOut accountPersistence) {
        return new UpdateAccountUseCase(accountPersistence);
    }

    @Bean
    public DeleteAccountPortIn deleteAccountPortIn(AccountPersistencePortOut accountPersistence) {
        return new DeleteAccountUseCase(accountPersistence);
    }
}
