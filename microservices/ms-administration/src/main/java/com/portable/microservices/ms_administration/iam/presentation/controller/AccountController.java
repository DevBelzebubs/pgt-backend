package com.portable.microservices.ms_administration.iam.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.ports.in.DeleteAccountPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.GetAccountPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.ListAccountsPortIn;
import com.portable.microservices.ms_administration.iam.domain.ports.in.UpdateAccountPortIn;
import com.portable.microservices.ms_administration.iam.presentation.dto.AccountResponse;
import com.portable.microservices.ms_administration.iam.presentation.dto.UpdateAccountRequest;
import com.portable.shared.infrastructure.presentation.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Exposes CRUD operations for accounts.
 * Account creation is handled by POST /api/v1/users (CreateUserUseCase),
 * which atomically creates the user + account in one operation.
 * All endpoints here require a valid JWT token.
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final ListAccountsPortIn listAccountsUseCase;
    private final GetAccountPortIn getAccountUseCase;
    private final UpdateAccountPortIn updateAccountUseCase;
    private final DeleteAccountPortIn deleteAccountUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> list() {
        List<AccountResponse> responses = listAccountsUseCase.execute().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Cuentas listadas", responses));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<AccountResponse>> get(@PathVariable UUID uuid) {
        Account account = getAccountUseCase.execute(uuid);
        return ResponseEntity.ok(ApiResponse.ok("Cuenta encontrada", toResponse(account)));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<AccountResponse>> update(@PathVariable UUID uuid,
                                                                @Valid @RequestBody UpdateAccountRequest request) {
        var command = new UpdateAccountPortIn.UpdateAccountCommand(
                uuid,
                request.headquarterId(),
                request.active()
        );
        Account updated = updateAccountUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ok("Cuenta actualizada", toResponse(updated)));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID uuid) {
        deleteAccountUseCase.execute(uuid);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok("Cuenta eliminada", null));
    }

    private AccountResponse toResponse(Account account) {
        String firstName = account.user() != null ? account.user().firstName() : null;
        String lastName  = account.user() != null ? account.user().lastName()  : null;
        return new AccountResponse(
                account.uuid(),
                account.username(),
                firstName,
                lastName,
                account.active(),
                account.headquarterId(),
                account.createdAt()
        );
    }
}
