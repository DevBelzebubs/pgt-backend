package com.portable.microservices.ms_administration.iam.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.portable.microservices.ms_administration.iam.application.usecases.CreateUserUseCase;
import com.portable.microservices.ms_administration.iam.domain.ports.in.CreateUserPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.PasswordEncryptationPortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;

@Configuration
public class IamConfig {
    
    @Bean
    public CreateUserPortIn createUserPortIn(UserPersistencePortOut userPersistence, AccountPersistencePortOut accountPersistence, RolePersistencePortOut rolePersistence, PasswordEncryptationPortOut passwordEncryptation, JdbcTemplate jdbcTemplate) {
        return new CreateUserUseCase(userPersistence, accountPersistence, rolePersistence, passwordEncryptation, jdbcTemplate);
    }
}
