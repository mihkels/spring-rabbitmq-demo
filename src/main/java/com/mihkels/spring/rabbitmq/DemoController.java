package com.mihkels.spring.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class DemoController {
    private static final Logger log = LoggerFactory.getLogger(DemoController.class);
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    private final AsyncRabbitTemplate rabbitTemplate;

    public DemoController(AsyncRabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping(value = "/")
    public Mono<String> checkRabbitMq(@RequestBody final String name) {
        final String currentCount = Integer.toString(atomicInteger.incrementAndGet());
        final String fullRequest = currentCount + " " + name;
        log.info("Request coming in: {}", fullRequest);

        return asyncProcessRequest(fullRequest);
    }

    private Mono<String> asyncProcessRequest(final String fullRequest) {
        final ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<>() {};
        final CompletableFuture<String> converterFuture = rabbitTemplate.convertSendAndReceiveAsType(
                RabbitConfiguration.QUEUE_NAME,
                fullRequest,
                responseType
        );

        return Mono.fromFuture(converterFuture);
    }
}
