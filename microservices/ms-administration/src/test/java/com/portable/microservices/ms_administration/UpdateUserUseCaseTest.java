package com.portable.microservices.ms_administration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portable.microservices.ms_administration.iam.application.usecases.UpdateUserUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.model.User;
import com.portable.microservices.ms_administration.iam.domain.ports.in.UpdateUserPortIn.UpdateUserCommand;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

    @Mock
    private UserPersistencePortOut userPersistence;
    @Mock
    private RolePersistencePortOut rolePersistence;
    @Mock
    private AccountPersistencePortOut accountPersistence;

    @InjectMocks
    private UpdateUserUseCase updateUserUseCase;

    @Test
    @DisplayName("Debe actualizar el usuario y su sede en la cuenta correctamente")
    void execute_Success() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        UpdateUserCommand command = new UpdateUserCommand(
                uuid, "John", "Doe", 2L, "ADMIN"
        );

        Role role = new Role(1L, "ADMIN", null);
        User existingUser = new User(100L, uuid, "Old", "User", "12345678", Set.of(role), ZonedDateTime.now(), ZonedDateTime.now());
        Account existingAccount = new Account(1L, UUID.randomUUID(), existingUser, 100L, 1L, "jdoe", "pass", true, ZonedDateTime.now());

        when(userPersistence.findByUuid(uuid)).thenReturn(Optional.of(existingUser));
        when(rolePersistence.findByName("ADMIN")).thenReturn(Optional.of(role));
        when(userPersistence.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(accountPersistence.findAll()).thenReturn(List.of(existingAccount));

        // Act
        User result = updateUserUseCase.execute(command);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.firstName());
        assertEquals("Doe", result.lastName());
        verify(userPersistence).save(any(User.class));
        verify(accountPersistence).save(any(Account.class));
    }

    @Test
    @DisplayName("Debe actualizar solo el nombre sin cambiar rol ni sede si son nulos en el comando")
    void execute_PartialUpdate_Success() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        UpdateUserCommand command = new UpdateUserCommand(uuid, "NewName", null, null, null);

        Role role = new Role(1L, "ADMIN", null);
        User existingUser = new User(100L, uuid, "Old", "User", "12345678", Set.of(role), ZonedDateTime.now(), ZonedDateTime.now());

        when(userPersistence.findByUuid(uuid)).thenReturn(Optional.of(existingUser));
        when(userPersistence.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        User result = updateUserUseCase.execute(command);

        // Assert
        assertEquals("NewName", result.firstName());
        assertEquals("User", result.lastName());
        assertEquals(Set.of(role), result.roles());
        verify(accountPersistence, never()).findAll();
    }

    @Test
    @DisplayName("Debe lanzar excepción si el usuario no existe")
    void execute_UserNotFound_ThrowsException() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        UpdateUserCommand command = new UpdateUserCommand(uuid, "John", "Doe", 1L, "ADMIN");
        when(userPersistence.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> updateUserUseCase.execute(command));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el rol especificado no existe")
    void execute_RoleNotFound_ThrowsException() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        UpdateUserCommand command = new UpdateUserCommand(uuid, "John", "Doe", 1L, "INVALID_ROLE");
        User existingUser = new User(100L, uuid, "Old", "User", "12345678", Set.of(), ZonedDateTime.now(), ZonedDateTime.now());

        when(userPersistence.findByUuid(uuid)).thenReturn(Optional.of(existingUser));
        when(rolePersistence.findByName("INVALID_ROLE")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> updateUserUseCase.execute(command));
    }
}