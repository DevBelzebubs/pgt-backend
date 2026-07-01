package com.portable.microservices.ms_administration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portable.microservices.ms_administration.iam.application.usecases.DeleteUserUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.model.User;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {

    @Mock
    private UserPersistencePortOut userPersistence;

    @Mock
    private AccountPersistencePortOut accountPersistence;

    @InjectMocks
    private DeleteUserUseCase deleteUserUseCase;

    @Test
    @DisplayName("Debe eliminar el usuario y sus cuentas asociadas exitosamente")
    void execute_WithExistingUserAndAccount_ShouldDeleteBoth() {
        // Arrange
        UUID userUuid = UUID.randomUUID();
        Long userId = 1L;
        User user = new User(userId, userUuid, "John", "Doe", "12345678", null, null, null);

        UUID accountUuid = UUID.randomUUID();
        Account account = new Account(10L, accountUuid, user, userId, 1L, "johndoe", "pass", true, null);

        when(userPersistence.findByUuid(userUuid)).thenReturn(Optional.of(user));
        when(accountPersistence.findAll()).thenReturn(List.of(account));

        // Act
        deleteUserUseCase.execute(userUuid);

        // Assert
        verify(accountPersistence).deleteByUuid(accountUuid);
        verify(userPersistence).deleteById(String.valueOf(userId));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException si el usuario no existe")
    void execute_WithNonExistingUser_ShouldThrowException() {
        UUID uuid = UUID.randomUUID();
        when(userPersistence.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> deleteUserUseCase.execute(uuid));
        verify(accountPersistence, never()).findAll();
        verify(userPersistence, never()).deleteById(anyString());
    }
}