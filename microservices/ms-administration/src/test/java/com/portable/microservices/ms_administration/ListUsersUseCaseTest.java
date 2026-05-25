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

import com.portable.microservices.ms_administration.iam.application.usecases.ListUsersUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.User;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;

@ExtendWith(MockitoExtension.class)
class ListUsersUseCaseTest {

    @Mock
    private UserPersistencePortOut userPersistence;

    @InjectMocks
    private ListUsersUseCase listUsersUseCase;

    @Test
    @DisplayName("Debe retornar una lista de usuarios")
    void execute_ShouldReturnListOfUsers() {
        // Arrange
        List<User> expectedUsers = List.of(mock(User.class), mock(User.class));
        when(userPersistence.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> result = listUsersUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(expectedUsers, result);
        verify(userPersistence).findAll();
    }
}