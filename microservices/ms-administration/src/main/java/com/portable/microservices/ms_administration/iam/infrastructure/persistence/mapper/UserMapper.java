package com.portable.microservices.ms_administration.iam.infrastructure.persistence.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.portable.microservices.ms_administration.iam.domain.model.Role;
import com.portable.microservices.ms_administration.iam.domain.model.User;
import com.portable.microservices.ms_administration.iam.infrastructure.persistence.entity.RoleJpaEntity;
import com.portable.microservices.ms_administration.iam.infrastructure.persistence.entity.UserJpaEntity;

@Component
public class UserMapper {

    public UserJpaEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        Set<RoleJpaEntity> roleEntities = null;

        if (user.roles() != null) {
            roleEntities = user.roles().stream()
                    .map(r -> {
                        RoleJpaEntity entity = new RoleJpaEntity();
                        entity.setId(r.id());
                        entity.setName(r.name());
                        return entity;
                    })
                    .collect(Collectors.toSet());
        }

        return UserJpaEntity.builder()
                .id(user.id())
                .uuid(user.uuid())
                .firstName(user.firstName())
                .lastName(user.lastName())
                .dni(user.dni())
                .roles(roleEntities)
                .build();
    }

    public User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Set<Role> roles = null;
        if (entity.getRoles() != null) {
            roles = entity.getRoles().stream()
                    .map(r -> new Role(r.getId(), r.getName(), r.getCreatedAt()))
                    .collect(Collectors.toSet());
        }

        return new User(
                entity.getId(),
                entity.getUuid(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDni(),
                roles,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
