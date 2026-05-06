package com.portable.microservices.ms_administration.iam.application.usecases;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.model.User;
import com.portable.microservices.ms_administration.iam.domain.ports.in.UpdateUserPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;

import lombok.RequiredArgsConstructor;

import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.model.Account;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class UpdateUserUseCase implements UpdateUserPortIn {
    private final UserPersistencePortOut userPersistence;
    private final RolePersistencePortOut rolePersistence;
    private final AccountPersistencePortOut accountPersistence;

    @Override
    @Transactional
    public User execute(UpdateUserCommand command) {
        UUID uuid = command.uuid();
        User existing = userPersistence.findByUuid(uuid).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Set<Role> roles = existing.roles();
        if (command.roleName() != null && !command.roleName().isBlank()) {
            Role role = rolePersistence.findByName(command.roleName()).orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
            roles = Set.of(role);
        }

        User updated = new User(
            existing.id(),
            existing.uuid(),
            command.firstName() != null ? command.firstName() : existing.firstName(),
            command.lastName() != null ? command.lastName() : existing.lastName(),
            existing.dni(),
            roles,
            existing.createdAt(),
            ZonedDateTime.now()
        );

        User savedUser = userPersistence.save(updated);

        // Update headquarter in the associated account
        if (command.headquarterId() != null) {
            accountPersistence.findAll().stream()
                .filter(a -> a.userId() != null && a.userId().equals(savedUser.id()))
                .findFirst()
                .ifPresent(acc -> {
                    Account updatedAcc = new Account(
                        acc.id(), acc.uuid(), acc.user(), acc.userId(),
                        command.headquarterId(), acc.username(), acc.password(),
                        acc.active(), acc.createdAt()
                    );
                    accountPersistence.save(updatedAcc);
                });
        }

        return savedUser;
    }
}
