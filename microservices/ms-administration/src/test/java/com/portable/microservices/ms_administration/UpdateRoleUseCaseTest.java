package com.portable.microservices.ms_administration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import com.portable.microservices.ms_administration.iam.application.usecases.UpdateRoleUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;

@ExtendWith(MockitoExtension.class)
class UpdateRoleUseCaseTest {

    @Mock
    private RolePersistencePortOut rolePersistence;

    @InjectMocks
    private UpdateRoleUseCase updateRoleUseCase;

    @Test
    @DisplayName("Debe actualizar el rol exitosamente cuando los datos son válidos")
    void execute_WithValidData_ShouldUpdateRole() {
        // Arrange
        Long roleId = 1L;
        String newName = "  SUPER_ADMIN  ";
        String trimmedName = "SUPER_ADMIN";
        
        Role existingRole = new Role(roleId, "ADMIN", null);
        Role updatedRole = new Role(roleId, trimmedName, null);

        when(rolePersistence.findById(roleId)).thenReturn(Optional.of(existingRole));
        when(rolePersistence.findByName(trimmedName)).thenReturn(Optional.empty());
        when(rolePersistence.save(any(Role.class))).thenReturn(updatedRole);

        // Act
        Role result = updateRoleUseCase.execute(roleId, newName);

        // Assert
        assertNotNull(result);
        assertEquals(trimmedName, result.name());
        verify(rolePersistence).findById(roleId);
        verify(rolePersistence).findByName(trimmedName);
        verify(rolePersistence).save(any(Role.class));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el rol no existe")
    void execute_WithNonExistingRole_ShouldThrowException() {
        // Arrange
        Long roleId = 1L;
        when(rolePersistence.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> updateRoleUseCase.execute(roleId, "NEW_NAME"));
        verify(rolePersistence, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el nombre ya está tomado por otro rol")
    void execute_WithNameAlreadyTakenByOtherRole_ShouldThrowException() {
        // Arrange
        Long roleId = 1L;
        Long otherRoleId = 2L;
        String nameTaken = "MANAGER";
        when(rolePersistence.findById(roleId)).thenReturn(Optional.of(new Role(roleId, "ADMIN", null)));
        when(rolePersistence.findByName(nameTaken)).thenReturn(Optional.of(new Role(otherRoleId, nameTaken, null)));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> updateRoleUseCase.execute(roleId, nameTaken));
        verify(rolePersistence, never()).save(any());
    }
}