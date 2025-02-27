package com.eazybytes.loans.rabbitmq;

import com.eazybytes.loans.dto.CustomerDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqConsumer {

    @RabbitListener(queues = RabbitMqConfig.ACCOUNTS_QUEUE_NAME)
    public void reciveMessage(CustomerDto message){
        System.out.println("Recived message in Queue: " + RabbitMqConfig.ACCOUNTS_QUEUE_NAME + " Message: " + message);
    }
}
