package com.mindhub.order_service.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "exchange-order",
            ORDER_CREATE_ORDER_QUEUE = "createOrder",
            ORDER_CONFIRM_ORDER_QUEUE = "confirmOrder",
            ORDER_CREATE_ORDER_KEY = "orderEmail.key",
            ORDER_CONFIRM_ORDER_KEY = "confirmOrder.key";

    @Bean
    public Queue queue() {
        return new Queue(ORDER_CREATE_ORDER_QUEUE, true);
    }

    @Bean
    public Queue queue2() {
        return new Queue(ORDER_CONFIRM_ORDER_QUEUE, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(ORDER_CREATE_ORDER_KEY);
    }

    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(queue2()).to(exchange()).with(ORDER_CONFIRM_ORDER_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        return rabbitTemplate(connectionFactory, jsonMessageConverter());
    }
}
