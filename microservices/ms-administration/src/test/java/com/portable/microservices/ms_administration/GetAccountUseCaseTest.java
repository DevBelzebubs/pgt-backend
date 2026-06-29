package com.portable.microservices.ms_administration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portable.microservices.ms_administration.iam.application.usecases.GetAccountUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;

@ExtendWith(MockitoExtension.class)
class GetAccountUseCaseTest {

    @Mock
    private AccountPersistencePortOut accountPersistence;

    @InjectMocks
    private GetAccountUseCase getAccountUseCase;

    @Test
    @DisplayName("Debe retornar la cuenta cuando el UUID existe")
    void execute_WithExistingUuid_ShouldReturnAccount() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Account account = mock(Account.class);
        when(accountPersistence.findByUuid(uuid)).thenReturn(Optional.of(account));

        // Act
        Account result = getAccountUseCase.execute(uuid);

        // Assert
        assertNotNull(result);
        assertEquals(account, result);
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el UUID no existe")
    void execute_WithNonExistingUuid_ShouldThrowException() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(accountPersistence.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> getAccountUseCase.execute(uuid));
    }
}