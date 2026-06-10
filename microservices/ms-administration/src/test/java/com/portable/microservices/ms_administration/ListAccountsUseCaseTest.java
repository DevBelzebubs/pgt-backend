package com.portable.microservices.ms_administration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portable.microservices.ms_administration.iam.application.usecases.ListAccountsUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;

@ExtendWith(MockitoExtension.class)
class ListAccountsUseCaseTest {

    @Mock
    private AccountPersistencePortOut accountPersistence;

    @InjectMocks
    private ListAccountsUseCase listAccountsUseCase;

    @Test
    @DisplayName("Debe retornar una lista de cuentas")
    void execute_ShouldReturnListOfAccounts() {
        // Arrange
        List<Account> expectedAccounts = List.of(mock(Account.class), mock(Account.class));
        when(accountPersistence.findAll()).thenReturn(expectedAccounts);

        // Act
        List<Account> result = listAccountsUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(expectedAccounts, result);
        verify(accountPersistence).findAll();
    }
}