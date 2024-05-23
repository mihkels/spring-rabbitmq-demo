package com.mihkels.spring.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HelloService {
    private static final Logger log = LoggerFactory.getLogger(HelloService.class);

    @RabbitListener(queues = RabbitConfiguration.QUEUE_NAME)
    public String process(@Payload String payload) {
        final String replay = new Date() + " " + "Hello " + payload;
        log.info("Processed message: {}", replay);

        return replay;
    }

}
