package com.portable.microservices.ms_administration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portable.microservices.ms_administration.iam.application.usecases.DeleteAccountUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;

@ExtendWith(MockitoExtension.class)
class DeleteAccountUseCaseTest {

    @Mock
    private AccountPersistencePortOut accountPersistence;

    @InjectMocks
    private DeleteAccountUseCase deleteAccountUseCase;

    @Test
    @DisplayName("Debe eliminar una cuenta exitosamente cuando el UUID existe")
    void execute_WithExistingUuid_ShouldDeleteAccount() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(accountPersistence.findByUuid(uuid)).thenReturn(Optional.of(mock(Account.class)));

        // Act
        deleteAccountUseCase.execute(uuid);

        // Assert
        verify(accountPersistence).deleteByUuid(uuid);
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el UUID no existe")
    void execute_WithNonExistingUuid_ShouldThrowException() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(accountPersistence.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> deleteAccountUseCase.execute(uuid));
        verify(accountPersistence, never()).deleteByUuid(uuid);
    }
}