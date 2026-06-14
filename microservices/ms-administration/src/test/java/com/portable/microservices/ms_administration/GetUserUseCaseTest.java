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

import com.portable.microservices.ms_administration.iam.application.usecases.GetUserUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.User;
import com.portable.microservices.ms_administration.iam.domain.ports.out.UserPersistencePortOut;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseTest {

    @Mock
    private UserPersistencePortOut userPersistence;

    @InjectMocks
    private GetUserUseCase getUserUseCase;

    @Test
    @DisplayName("Debe retornar el usuario cuando el UUID existe")
    void execute_WithExistingUuid_ShouldReturnUser() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        User user = mock(User.class);
        when(userPersistence.findByUuid(uuid)).thenReturn(Optional.of(user));

        // Act
        User result = getUserUseCase.execute(uuid);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el UUID no existe")
    void execute_WithNonExistingUuid_ShouldThrowException() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(userPersistence.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> getUserUseCase.execute(uuid));
    }
}