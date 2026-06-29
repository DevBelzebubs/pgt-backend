package com.portable.microservices.ms_administration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portable.microservices.ms_administration.iam.application.usecases.GetRoleUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;

@ExtendWith(MockitoExtension.class)
class GetRoleUseCaseTest {

    @Mock
    private RolePersistencePortOut rolePersistence;

    @InjectMocks
    private GetRoleUseCase getRoleUseCase;

    @Test
    @DisplayName("Debe retornar el rol cuando el ID existe")
    void execute_WithExistingId_ShouldReturnRole() {
        // Arrange
        Long roleId = 1L;
        Role role = mock(Role.class);
        when(rolePersistence.findById(roleId)).thenReturn(Optional.of(role));

        // Act
        Role result = getRoleUseCase.execute(roleId);

        // Assert
        assertNotNull(result);
        assertEquals(role, result);
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el ID no existe")
    void execute_WithNonExistingId_ShouldThrowException() {
        // Arrange
        Long roleId = 1L;
        when(rolePersistence.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> getRoleUseCase.execute(roleId));
    }
}