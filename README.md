# Sample Spring RabbitMQ web application

Spring sample web application showing how to use `DeferredResult` and **Spring AMQP 1.6.1.RELEASE** `AsyncRabbitTemplate`

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

Then in a different shell pass some parameter to the application with

```bash
curl -X POST localhost:8080 -d 'foo'
```

and study the output of the application; you should see lines like

```
2016-07-26 10:46:00.782  INFO 20907 --- [nio-8080-exec-1] com.mihkels.DemoController               : Request coming in: 1 foo=
2016-07-26 10:46:00.810  INFO 20907 --- [cTaskExecutor-1] com.mihkels.HelloService                 : Processed message: Tue Jul 26 10:46:00 CEST 2016 Hello 1 foo=
2016-07-26 10:46:00.814  INFO 20907 --- [enerContainer-1] com.mihkels.DemoController               : Output response: Tue Jul 26 10:46:00 CEST 2016 Hello 1 foo=
```