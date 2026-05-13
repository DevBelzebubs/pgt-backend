package com.portable.microservices.ms_administration.iam.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.model.User;
import com.portable.microservices.ms_administration.iam.presentation.dto.UserAccountResponse;
import com.portable.microservices.ms_administration.iam.presentation.dto.UserResponse;

@Component
public class UserPresentationMapper {

    public UserAccountResponse toResponse(Account account) {
        if (account == null) {
            return null;
        }

        String firstName = null;
        String lastName = null;
        String roleName = null;

        if (account.user() != null) {
            firstName = account.user().firstName();
            lastName = account.user().lastName();
            
            if (account.user().roles() != null && !account.user().roles().isEmpty()) {
                Role firstRole = account.user().roles().iterator().next();
                roleName = firstRole.name();
            }
        }

        return new UserAccountResponse(
                account.uuid(),
                account.username(),
                firstName,
                lastName,
                roleName,
                account.active()
        );
    }

    public UserResponse toResponse(User user) {
        if (user == null) return null;

        java.util.Set<String> roleNames = java.util.Collections.emptySet();
        if (user.roles() != null && !user.roles().isEmpty()) {
            roleNames = user.roles().stream().map(Role::name).collect(java.util.stream.Collectors.toSet());
        }

        return new UserResponse(
            user.uuid(),
            user.firstName(),
            user.lastName(),
            user.dni(),
            roleNames,
            user.createdAt(),
            user.updatedAt()
        );
    }
}
