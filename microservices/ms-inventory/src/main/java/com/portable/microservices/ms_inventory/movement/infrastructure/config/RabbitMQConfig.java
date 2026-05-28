package com.portable.microservices.ms_inventory.movement.infrastructure.config;

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

    public static final String INVENTORY_PICKING_QUEUE = "inventory.picking.queue";

    public static final String TRACKING_EXCHANGE = "tracking.exchange";

    public static final String ROUTING_KEY = "tracking.picking.completed";

    public static final String INVENTORY_EXCHANGE = "inventory.exchange";

    @Bean
    public Queue pickingQueue() {
        return new Queue(INVENTORY_PICKING_QUEUE, true);
    }

    @Bean
    public TopicExchange trackingExchange() {
        return new TopicExchange(TRACKING_EXCHANGE);
    }

    @Bean
    public Binding bindingPicking(Queue pickingQueue, TopicExchange trackingExchange) {
        return BindingBuilder.bind(pickingQueue).to(trackingExchange).with(ROUTING_KEY);
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

    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }
}