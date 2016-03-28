package com.mihkels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class DemoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Autowired
    private AsyncRabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public DeferredResult<String> checkRabbitMq(@RequestBody final String name) {
        final String currentCount = Integer.toString(atomicInteger.incrementAndGet());
        final String fullRequest = currentCount + " " + name;
        LOGGER.info("Request coming in: {}", fullRequest);

        return asyncProcessRequest(fullRequest);
    }

    private DeferredResult<String> asyncProcessRequest(final String fullRequest) {
        final DeferredResult<String> result = new DeferredResult<>();
        final ListenableFuture<String> converterFuture = rabbitTemplate.convertSendAndReceive("spring-boot", fullRequest);
        converterFuture.addCallback(new HandleRabbitResponse(result));

        return result;
    }

    private static class HandleRabbitResponse implements ListenableFutureCallback<String> {
        private final DeferredResult<String> result;

        HandleRabbitResponse(final DeferredResult<String> result) {
            this.result = result;
        }

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
