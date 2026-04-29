package com.portable.microservices.ms_administration.iam.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_administration.iam.domain.model.Account;
import com.portable.microservices.ms_administration.iam.infrastructure.persistence.entity.AccountJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountMapper {
    
    private final UserMapper userMapper;

    public AccountJpaEntity toEntity(Account account) {
        if (account == null) {
            return null;
        }

        return AccountJpaEntity.builder()
                .id(account.id())
                .uuid(account.uuid())
                // Delegamos la complejidad al mapper del usuario
                .user(userMapper.toEntity(account.user()))
                .headquarterId(account.headquarterId())
                .username(account.username())
                .passwordHash(account.password())
                .active(account.active())
                .build();
    }

    public Account toDomain(AccountJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Account(
                entity.getId(),
                entity.getUuid(),
                userMapper.toDomain(entity.getUser()),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getHeadquarterId(),
                entity.getUsername(),
                entity.getPasswordHash(),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }
}
