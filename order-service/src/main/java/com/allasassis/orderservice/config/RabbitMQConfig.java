package com.allasassis.orderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.order.name}")
    private String orderQueue;

    @Value("${rabbitmq.exchange.name}")
    private String orderExchange;

    @Value("${rabbitmq.binding.routing.key}")
    private String orderRoutingKey;

    @Value("${rabbitmq.queue.email.name}")
    private String orderEmailQueue;

    @Value("${rabbitmq.binding.email.routing.key}")
    private String orderEmailRoutingKey;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(orderExchange);
    }

    @Bean
    public Queue orderQueue() {
        return new Queue(orderQueue);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(orderQueue()).to(exchange()).with(orderRoutingKey);
    }

    @Bean
    public Queue orderEmailQueue() {
        return new Queue(orderEmailQueue);
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(orderEmailQueue()).to(exchange()).with(orderEmailRoutingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
