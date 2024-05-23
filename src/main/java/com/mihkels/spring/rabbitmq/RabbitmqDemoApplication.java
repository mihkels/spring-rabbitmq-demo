package com.mihkels.spring.rabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class RabbitmqDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(RabbitmqDemoApplication.class, args);
	}
}
