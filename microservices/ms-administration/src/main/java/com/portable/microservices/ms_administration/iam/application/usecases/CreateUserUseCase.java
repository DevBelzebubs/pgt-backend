package com.portable.microservices.ms_administration.iam.application.usecases;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate; // IMPORTANTE

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
    private final JdbcTemplate jdbcTemplate; // Inyectamos esto para el diagnóstico

    @Override
    @Transactional
    public Account execute(CreateUserCommand command) {
        
        // --- INICIO BLOQUE DE DIAGNÓSTICO ---
        System.out.println("========== TEST DE VISIBILIDAD BASE DE DATOS ==========");
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM administration.sede WHERE id_sede = ?", 
                Integer.class, 
                command.headquarterId()
            );
            System.out.println("🔍 ID BUSCADO: " + command.headquarterId());
            System.out.println("🔍 ¿JAVA VE LA SEDE EN LA DB?: " + (count > 0 ? "SÍ (Existe)" : "NO (No existe)"));
            
            // Verificamos en qué esquema y base estamos parados
            String currentDb = jdbcTemplate.queryForObject("SELECT current_database()", String.class);
            String currentSchema = jdbcTemplate.queryForObject("SELECT current_schema()", String.class);
            System.out.println("🔍 BASE DE DATOS ACTUAL: " + currentDb);
            System.out.println("🔍 ESQUEMA ACTUAL: " + currentSchema);
            
        } catch (Exception e) {
            System.err.println("❌ ERROR DURANTE EL DIAGNÓSTICO: " + e.getMessage());
        }
        System.out.println("=======================================================");
        // --- FIN BLOQUE DE DIAGNÓSTICO ---

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