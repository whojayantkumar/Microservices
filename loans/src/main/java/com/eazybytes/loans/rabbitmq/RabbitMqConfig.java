package com.eazybytes.loans.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String ACCOUNTS_QUEUE_NAME = "accounts.queue";
    public static final String ACCOUNTS_EXCHANGE_NAME = "accounts.exchange";
    public static final String ACCOUNTS_ROUTING_KEY = "accounts.routingKey";

    @Bean
    public Queue queue(){
        return new Queue(ACCOUNTS_QUEUE_NAME, true);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(ACCOUNTS_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with(ACCOUNTS_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitMqTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
