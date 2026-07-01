package com.portable.microservices.ms_administration.iam.infrastructure.messaging.listener;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.portable.microservices.ms_administration.iam.infrastructure.messaging.dto.GetUserNamesRequest;
import com.portable.microservices.ms_administration.iam.infrastructure.messaging.dto.GetUserNamesResponse;
import com.portable.microservices.ms_administration.iam.infrastructure.messaging.dto.UserNameDto;
import com.portable.microservices.ms_administration.iam.infrastructure.persistence.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserResolveRequestListener {
    private final UserJpaRepository userRepository;

    @RabbitListener(queuesToDeclare = @Queue(name = "admin.user.resolve.queue", durable = "true"))
    public GetUserNamesResponse handle(GetUserNamesRequest request) {
        var users = userRepository.findAllById(request.userIds());
        var dtos = users.stream()
                .map(u -> new UserNameDto(u.getId(),
                        u.getFirstName() + " " + u.getLastName()))
                .toList();
        return new GetUserNamesResponse(dtos);
    }
}