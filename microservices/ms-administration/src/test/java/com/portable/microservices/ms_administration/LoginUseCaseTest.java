package com.portable.microservices.ms_administration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.portable.microservices.ms_administration.iam.application.usecases.LoginUseCase;
import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.model.User;
import com.portable.microservices.ms_administration.iam.domain.ports.out.AccountPersistencePortOut;
import com.portable.microservices.ms_administration.iam.domain.ports.out.PasswordEncryptationPortOut;
import com.portable.microservices.ms_administration.iam.infrastructure.security.jwt.JwtService;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private AccountPersistencePortOut accountPersistence;

    @Mock
    private PasswordEncryptationPortOut passwordEncryptation;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginUseCase loginUseCase;

    @Test
    @DisplayName("Debe retornar un token cuando las credenciales son válidas")
    void execute_WithValidCredentials_ShouldReturnToken() {
        // Arrange
        String username = "admin";
        String password = "password123";
        String encryptedPassword = "hashed_password";
        String expectedToken = "jwt-token-example";

        Role role = new Role(1L, "ADMIN", null);
        User user = new User(1L, UUID.randomUUID(), "Test", "User", "12345678", Set.of(role), null, null);
        Account account = new Account(1L, UUID.randomUUID(), user, 1L, 100L, username, encryptedPassword, true, null);

        when(accountPersistence.findByUsername(username)).thenReturn(Optional.of(account));
        when(passwordEncryptation.matches(password, encryptedPassword)).thenReturn(true);
        when(jwtService.createToken(username, "ADMIN")).thenReturn(expectedToken);

        // Act
        String result = loginUseCase.execute(username, password);

        // Assert
        assertNotNull(result);
        assertEquals(expectedToken, result);
        verify(accountPersistence).findByUsername(username);
        verify(passwordEncryptation).matches(password, encryptedPassword);
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException si el usuario no existe")
    void execute_WithInvalidUser_ShouldThrowException() {
        when(accountPersistence.findByUsername("unknown")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> loginUseCase.execute("unknown", "password"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException si la contraseña es incorrecta")
    void execute_WithWrongPassword_ShouldThrowException() {
        Account account = new Account(1L, UUID.randomUUID(), null, 1L, 1L, "user", "hashed", true, null);
        when(accountPersistence.findByUsername("user")).thenReturn(Optional.of(account));
        when(passwordEncryptation.matches(anyString(), anyString())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> loginUseCase.execute("user", "wrongpass"));
    }
}