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

import com.portable.microservices.ms_administration.iam.application.usecases.ListRolesUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;

@ExtendWith(MockitoExtension.class)
class ListRolesUseCaseTest {

    @Mock
    private RolePersistencePortOut rolePersistence;

    @InjectMocks
    private ListRolesUseCase listRolesUseCase;

    @Test
    @DisplayName("Debe retornar una lista de roles")
    void execute_ShouldReturnListOfRoles() {
        // Arrange
        List<Role> expectedRoles = List.of(mock(Role.class), mock(Role.class));
        when(rolePersistence.findAll()).thenReturn(expectedRoles);

        // Act
        List<Role> result = listRolesUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(expectedRoles, result);
        verify(rolePersistence).findAll();
    }
}