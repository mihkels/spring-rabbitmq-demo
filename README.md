# Sample Spring RabbitMQ web application

Spring sample web application showing how to use `DeferredResult` and **Spring AMQP 1.6.0.M2** `AsyncRabbitTemplate`

## Building project

Sample project is standard Spring Boot project created with [Spring Initializer](https://start.spring.io/) and 
recommended way of building the project is using Maven Wrapper by issuing below command: 

```bash
./mvnw install
```

## Running sample applications

First we need to install and start [RabbitMQ](https://www.rabbitmq.com/). 
If You are using Mac OS X then just issue below commands:

```bash
brew install rabbitmq
rabbitmq-server
```

Now when build succseeded issue below command to start the sample application:

```bash
java -jar target/rabbitmq-demo-0.0.1-SNAPSHOT.jar
```
