package com.example.axon.app;

import java.util.Collections;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.axonframework.amqp.eventhandling.DefaultAMQPMessageConverter;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.commandhandling.gateway.IntervalRetryScheduler;
import org.axonframework.commandhandling.gateway.RetryScheduler;
import org.axonframework.serialization.Serializer;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.rabbitmq.client.Channel;

@SpringBootApplication 
@ComponentScan(basePackages="com.example.axon")
public class SpringBootApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootApp.class, args);
	}
	
	@Bean
	public RetryScheduler getRetryScheduler() {
		RetryScheduler scheduler = new IntervalRetryScheduler(new ScheduledThreadPoolExecutor(10), 1000, 3);
		return scheduler;
	}
 
	@Autowired
	@Bean
	public CommandGateway commandGateway(CommandBus commandBus) {
		return new DefaultCommandGateway(commandBus, getRetryScheduler(), Collections.emptyList());
	}
	
	
	@Bean
	public Exchange exchange(@Value("${axon.amqp.exchange}") String name) {
		Exchange exchange = ExchangeBuilder.fanoutExchange(name).build();
		return exchange;
	}

	@Bean
	public Queue queue(@Value("${axon.amqp.queue}") String name) {
		Queue queue = QueueBuilder.durable(name).build();
		return queue;
	}

	@Bean
	public Binding binding(Exchange exchange, Queue queue) {
		Binding binding = BindingBuilder.bind(queue).to(exchange).with("*").noargs();
		return binding;
	}

	@Autowired
	public void configure(AmqpAdmin admin, Exchange exchange, Queue queue, Binding binding) {
		admin.declareExchange(exchange);
		admin.declareQueue(queue);
		admin.declareBinding(binding);
	}

	@Bean
	public SpringAMQPMessageSource menuMessageSource(Serializer serializer) {
		return new SpringAMQPMessageSource(new DefaultAMQPMessageConverter(serializer)) {
			@Override
			@RabbitListener(queues = "ApplicationEvents")
			public void onMessage(Message message, Channel channel) throws Exception {
				super.onMessage(message, channel);
			}
		};
	}
}
