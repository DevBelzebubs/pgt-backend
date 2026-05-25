package com.portable.microservices.ms_administration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portable.microservices.ms_administration.iam.application.usecases.DeleteRoleUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;

@ExtendWith(MockitoExtension.class)
class DeleteRoleUseCaseTest {

    @Mock
    private RolePersistencePortOut rolePersistence;

    @InjectMocks
    private DeleteRoleUseCase deleteRoleUseCase;

    @Test
    @DisplayName("Debe eliminar un rol exitosamente cuando el ID existe")
    void execute_WithExistingId_ShouldDeleteRole() {
        // Arrange
        Long roleId = 1L;
        when(rolePersistence.findById(roleId)).thenReturn(Optional.of(mock(Role.class)));

        // Act
        deleteRoleUseCase.execute(roleId);

        // Assert
        verify(rolePersistence).deleteById(roleId);
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el ID no existe")
    void execute_WithNonExistingId_ShouldThrowException() {
        // Arrange
        Long roleId = 99L;
        when(rolePersistence.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> deleteRoleUseCase.execute(roleId));
        verify(rolePersistence, never()).deleteById(roleId);
    }
}