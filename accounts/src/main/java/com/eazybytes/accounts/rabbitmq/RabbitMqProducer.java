package com.eazybytes.accounts.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqProducer {

    private AmqpTemplate rabbitTemplate;

    public <T> void sendMessage(T message){
        System.out.println("Sending message to : " + RabbitMqConfig.ACCOUNTS_QUEUE_NAME + " Message: " + message);
        rabbitTemplate.convertAndSend(RabbitMqConfig.ACCOUNTS_EXCHANGE_NAME, RabbitMqConfig.ACCOUNTS_ROUTING_KEY, message);
    }

    @Autowired
    public void setRabbitTemplate(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
}
