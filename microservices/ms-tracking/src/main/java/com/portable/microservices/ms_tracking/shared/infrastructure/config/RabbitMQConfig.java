package com.portable.microservices.ms_tracking.shared.infrastructure.config;

// IMPORTANTE: Estos son los imports correctos para Spring AMQP
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String TRACKING_QUEUE = "tracking.heatmap.queue";
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";
    public static final String ROUTING_KEY = "inventory.movement.created";
    @Bean
    public Queue heatmapQueue() {
        return new Queue(TRACKING_QUEUE, true);
    }

    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }

    @Bean
    public Binding bindingHeatmap(Queue heatmapQueue, TopicExchange inventoryExchange) {
        return BindingBuilder.bind(heatmapQueue).to(inventoryExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}