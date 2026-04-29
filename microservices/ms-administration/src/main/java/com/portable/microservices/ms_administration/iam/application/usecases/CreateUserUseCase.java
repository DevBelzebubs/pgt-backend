package com.portable.microservices.ms_administration.iam.application.usecases;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.model.User;
import com.portable.microservices.ms_administration.iam.domain.ports.in.CreateUserPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.PasswordEncryptationPortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateUserUseCase implements CreateUserPortIn {

    private final UserPersistencePortOut userPersistence;
    private final AccountPersistencePortOut accountPersistence;
    private final RolePersistencePortOut rolePersistence;
    private final PasswordEncryptationPortOut passwordEncryptation;

    @Override
    @Transactional
    public Account execute(CreateUserCommand command) {

        if (userPersistence.findByDni(command.dni()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el DNI: " + command.dni());
        }
        if (accountPersistence.existsByUsername(command.username())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso: " + command.username());
        }

        System.out.println("Buscando el rol exacto: [" + command.roleName() + "]");
        Role role = rolePersistence.findByName(command.roleName())
                .orElseThrow(() -> new IllegalArgumentException("El rol " + command.roleName() + " no existe."));

        User newUser = new User(
                null,
                UUID.randomUUID(),
                command.firstName(),
                command.lastName(),
                command.dni(),
                Set.of(role),
                ZonedDateTime.now(),
                ZonedDateTime.now()
        );

        User savedUser = userPersistence.save(newUser);
        String hashedPassword = passwordEncryptation.encrypt(command.plainPassword());

        Account newAccount = new Account(
                null,
                UUID.randomUUID(),
                savedUser,
                savedUser.id(),
                command.headquarterId(),
                command.username(),
                hashedPassword,
                true,
                ZonedDateTime.now()
        );

        return accountPersistence.save(newAccount);
    }
}