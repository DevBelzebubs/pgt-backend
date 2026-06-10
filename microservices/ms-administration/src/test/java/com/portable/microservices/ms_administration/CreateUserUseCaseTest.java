package com.portable.microservices.ms_administration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portable.microservices.ms_administration.iam.application.usecases.CreateUserUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.model.User;
import com.portable.microservices.ms_administration.iam.domain.ports.in.CreateUserPortIn.CreateUserCommand;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.PasswordEncryptationPortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserPersistencePortOut userPersistence;
    @Mock
    private AccountPersistencePortOut accountPersistence;
    @Mock
    private RolePersistencePortOut rolePersistence;
    @Mock
    private PasswordEncryptationPortOut passwordEncryptation;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Test
    @DisplayName("Debe crear un usuario y una cuenta exitosamente")
    void execute_Success() {
        // Se ajusta el constructor al orden: firstName, lastName, dni, username, plainPassword, headquarterId, roleName
        CreateUserCommand command = new CreateUserCommand(
                "John",      // firstName
                "Doe",       // lastName
                "12345678",  // dni
                "jdoe",      // username
                "secret123", // plainPassword
                1L,          // headquarterId (Long)
                "ADMIN"      // roleName (String)
        );

        Role role = new Role(1L, "ADMIN", null);
        // El usuario guardado debe tener un ID para que el caso de uso pueda crear la cuenta
        User savedUser = new User(100L, UUID.randomUUID(), "John", "Doe", "12345678", Set.of(role), ZonedDateTime.now(), ZonedDateTime.now());
        Account savedAccount = new Account(1L, UUID.randomUUID(), savedUser, 100L, 1L, "jdoe", "encrypted_pass", true, ZonedDateTime.now());

        when(userPersistence.findByDni(command.dni())).thenReturn(Optional.empty());
        when(accountPersistence.existsByUsername(command.username())).thenReturn(false);
        when(rolePersistence.findByName(command.roleName())).thenReturn(Optional.of(role));
        when(userPersistence.save(any(User.class))).thenReturn(savedUser);
        when(passwordEncryptation.encrypt(anyString())).thenReturn("encrypted_pass");
        when(accountPersistence.save(any(Account.class))).thenReturn(savedAccount);

        // Act
        Account result = createUserUseCase.execute(command);

        // Assert
        assertNotNull(result);
        assertEquals(savedAccount, result);
        verify(userPersistence).save(any(User.class));
        verify(accountPersistence).save(any(Account.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el DNI ya está registrado")
    void execute_DniAlreadyExists_ThrowsException() {
        // Arrange
        CreateUserCommand command = new CreateUserCommand(
                "John",
                "Doe",
                "12345678",
                "jdoe",
                "pass",
                1L,
                "ADMIN"
        );
        User existingUser = new User(1L, UUID.randomUUID(), "Other", "User", "12345678", null, null, null);
        
        when(userPersistence.findByDni("12345678")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> createUserUseCase.execute(command));
        
        assertTrue(exception.getMessage().contains("Ya existe un usuario con el DNI"));
        verify(userPersistence, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el nombre de usuario ya está en uso")
    void execute_UsernameAlreadyExists_ThrowsException() {
        // Arrange
        CreateUserCommand command = new CreateUserCommand(
                "John",
                "Doe",
                "12345678",
                "jdoe",
                "pass",
                1L,
                "ADMIN"
        );

        when(userPersistence.findByDni(any())).thenReturn(Optional.empty());
        when(accountPersistence.existsByUsername("jdoe")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> createUserUseCase.execute(command));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el rol no existe")
    void execute_RoleNotFound_ThrowsException() {
        // Arrange
        CreateUserCommand command = new CreateUserCommand(
                "John",
                "Doe",
                "12345678",
                "jdoe",
                "pass",
                1L,
                "GHOST_ROLE"
        );
        
        when(userPersistence.findByDni(any())).thenReturn(Optional.empty());
        when(accountPersistence.existsByUsername(any())).thenReturn(false);
        when(rolePersistence.findByName("GHOST_ROLE")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> createUserUseCase.execute(command));
    }
}
