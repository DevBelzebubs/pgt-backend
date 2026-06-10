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

import com.portable.microservices.ms_administration.iam.application.usecases.CreateRoleUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.ports.out.RolePersistencePortOut;

@ExtendWith(MockitoExtension.class)
class CreateRoleUseCaseTest {

    @Mock
    private RolePersistencePortOut rolePersistence;

    @InjectMocks
    private CreateRoleUseCase createRoleUseCase;

    @Test
    @DisplayName("Debe crear un rol exitosamente cuando el nombre es válido y no existe")
    void execute_WithValidName_ShouldCreateRole() {
        // Arrange
        String roleName = "  MANAGER  ";
        String trimmedName = "MANAGER";
        Role savedRole = new Role(1L, trimmedName, null);

        when(rolePersistence.findByName(trimmedName)).thenReturn(Optional.empty());
        when(rolePersistence.save(any(Role.class))).thenReturn(savedRole);

        // Act
        Role result = createRoleUseCase.execute(roleName);

        // Assert
        assertNotNull(result);
        assertEquals(trimmedName, result.name());
        verify(rolePersistence).findByName(trimmedName);
        verify(rolePersistence).save(any(Role.class));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el nombre está vacío o es nulo")
    void execute_WithBlankName_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> createRoleUseCase.execute(""));
        assertThrows(IllegalArgumentException.class, () -> createRoleUseCase.execute(null));
        assertThrows(IllegalArgumentException.class, () -> createRoleUseCase.execute("   "));
        
        verify(rolePersistence, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el rol ya existe")
    void execute_WithExistingRole_ShouldThrowException() {
        // Arrange
        String roleName = "ADMIN";
        Role existingRole = new Role(1L, roleName, null);
        when(rolePersistence.findByName(roleName)).thenReturn(Optional.of(existingRole));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> createRoleUseCase.execute(roleName));
        
        verify(rolePersistence, never()).save(any());
    }
}