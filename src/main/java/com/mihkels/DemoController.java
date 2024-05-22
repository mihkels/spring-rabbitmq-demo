package com.mihkels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class DemoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    private final AsyncRabbitTemplate rabbitTemplate;

    public DemoController(AsyncRabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping(value = "/")
    public DeferredResult<String> checkRabbitMq(@RequestBody final String name) {
        final String currentCount = Integer.toString(atomicInteger.incrementAndGet());
        final String fullRequest = currentCount + " " + name;
        LOGGER.info("Request coming in: {}", fullRequest);

        return asyncProcessRequest(fullRequest);
    }

    private DeferredResult<String> asyncProcessRequest(final String fullRequest) {
        final DeferredResult<String> result = new DeferredResult<>();
        final ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<>() {};

        final ListenableFuture<String> converterFuture = rabbitTemplate.convertSendAndReceiveAsType(
                RabbitConfiquration.QUEUE_NAME,
                fullRequest,
                (MessagePostProcessor) null,
                new SimpleMessageConverter(),
                responseType
        );
        converterFuture.addCallback(new HandleRabbitResponse(result));

        return result;
    }

    private record HandleRabbitResponse(DeferredResult<String> result) implements ListenableFutureCallback<String> {

        @Override
            public void onFailure(final Throwable throwable) {
                result.setResult("Failed to get response");
            }

            @Override
            public void onSuccess(final String response) {
                result.setResult(response);
                LOGGER.info("Output response: {}", result.getResult());
            }
        }
}
