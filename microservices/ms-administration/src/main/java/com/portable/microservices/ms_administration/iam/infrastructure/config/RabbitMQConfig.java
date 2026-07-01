package com.portable.microservices.ms_administration.iam.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";
    public static final String USER_RESOLVE_QUEUE = "admin.user.resolve.queue";
    public static final String USER_RESOLVE_ROUTING_KEY = "admin.user.resolve.request";

    @Bean
    public Queue userResolveQueue() {
        return new Queue(USER_RESOLVE_QUEUE, true);
    }

    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }

    @Bean
    public Binding userResolveBinding(Queue userResolveQueue, TopicExchange inventoryExchange) {
        return BindingBuilder.bind(userResolveQueue)
                .to(inventoryExchange).with(USER_RESOLVE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper mapper = new DefaultJackson2JavaTypeMapper();
        mapper.setTrustedPackages("*");
        converter.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        converter.setJavaTypeMapper(mapper);
        return converter;
    }
}