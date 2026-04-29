package com.portable.microservices.ms_administration.iam.application.usecases;

import org.springframework.stereotype.Service;

import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.ports.in.LoginPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.PasswordEncryptationPortOut;
import com.portable.microservices.ms_administration.iam.infrastructure.security.jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginUseCase implements LoginPortIn {
    private final AccountPersistencePortOut accountPersistence;
    private final PasswordEncryptationPortOut passwordEncryptation;
    private final JwtService jwtService;

    @Override
    public String execute(String username, String plainPassword) {
        Account account = accountPersistence.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!passwordEncryptation.matches(plainPassword, account.password())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        String roleName = account.user().roles().iterator().next().name();
        
        return jwtService.createToken(account.username(), roleName);
    }
}
