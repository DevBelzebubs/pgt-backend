package com.portable.microservices.ms_administration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portable.microservices.ms_administration.iam.application.usecases.UpdateAccountUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.ports.in.UpdateAccountPortIn.UpdateAccountCommand;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;

@ExtendWith(MockitoExtension.class)
class UpdateAccountUseCaseTest {

    @Mock
    private AccountPersistencePortOut accountPersistence;

    @InjectMocks
    private UpdateAccountUseCase updateAccountUseCase;

    @Test
    @DisplayName("Debe actualizar la cuenta exitosamente")
    void execute_WithValidData_ShouldUpdateAccount() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Long newHeadquarterId = 2L;
        boolean newActiveStatus = false;
        
        UpdateAccountCommand command = new UpdateAccountCommand(uuid, newHeadquarterId, newActiveStatus);
        
        Account existingAccount = new Account(
                1L, uuid, null, 10L, 1L, "user.test", "encrypted", true, ZonedDateTime.now()
        );
        
        Account updatedAccount = new Account(
                1L, uuid, null, 10L, newHeadquarterId, "user.test", "encrypted", newActiveStatus, existingAccount.createdAt()
        );

        when(accountPersistence.findByUuid(uuid)).thenReturn(Optional.of(existingAccount));
        when(accountPersistence.save(any(Account.class))).thenReturn(updatedAccount);

        // Act
        Account result = updateAccountUseCase.execute(command);

        // Assert
        assertNotNull(result);
        assertEquals(newHeadquarterId, result.headquarterId());
        assertEquals(newActiveStatus, result.active());
        verify(accountPersistence).findByUuid(uuid);
        verify(accountPersistence).save(any(Account.class));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando la cuenta no existe")
    void execute_WithNonExistingAccount_ShouldThrowException() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        UpdateAccountCommand command = new UpdateAccountCommand(uuid, 1L, true);
        when(accountPersistence.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> updateAccountUseCase.execute(command));
        verify(accountPersistence, never()).save(any());
    }
}