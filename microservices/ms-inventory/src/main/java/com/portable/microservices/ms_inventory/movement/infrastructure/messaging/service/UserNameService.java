package com.portable.microservices.ms_inventory.movement.infrastructure.messaging.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.portable.microservices.ms_inventory.movement.infrastructure.messaging.dto.GetUserNamesRequest;
import com.portable.microservices.ms_inventory.movement.infrastructure.messaging.dto.GetUserNamesResponse;
import com.portable.microservices.ms_inventory.movement.infrastructure.messaging.dto.UserNameDto;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserNameService {
    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE = "inventory.exchange";
    private static final String ROUTING_KEY = "admin.user.resolve.request";

    public Map<Long, String> resolveUserNames(Set<Long> userIds) {
        if (userIds.isEmpty())
            return Map.of();
        var request = new GetUserNamesRequest(List.copyOf(userIds));
        log.info("Enviando RPC a admin.user.resolve.request con {} userIds: {}", userIds.size(), userIds);
        try {
            var response = rabbitTemplate.convertSendAndReceiveAsType(
                    EXCHANGE, ROUTING_KEY, request,
                    new ParameterizedTypeReference<GetUserNamesResponse>() {
                    });
            log.info("Respuesta recibida: {}", response);
            if (response != null) {
                var map = response.users().stream()
                        .collect(Collectors.toMap(UserNameDto::id, UserNameDto::fullName));
                log.info("Mapa de nombres: {}", map);
                return map;
            }
            log.warn("Respuesta nula (timeout de 5s)");
        } catch (Exception e) {
            log.error("Error en RPC RabbitMQ: {}", e.getMessage(), e);
        }
        return Map.of();
    }
}