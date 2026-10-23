package com.example.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.messaging.Constants.RABBITMQ_DEAD_LETTER_DESTINATION_NAME;
import static com.example.messaging.Constants.RABBITMQ_DESTINATION_NAME;

@Configuration
class AmqpConfiguration {

	// <.>
	@Bean
	Queue emailsQueue() {
		return QueueBuilder.durable(RABBITMQ_DESTINATION_NAME)
			.deadLetterExchange("")
			.deadLetterRoutingKey(RABBITMQ_DEAD_LETTER_DESTINATION_NAME)
			.build();
	}

	// <.>
	@Bean
	Exchange emailsExchange() {
		return ExchangeBuilder.directExchange(RABBITMQ_DESTINATION_NAME).build();
	}

	// <.>
	@Bean
	Binding emailsBinding() {
		return BindingBuilder.bind(emailsQueue()).to(emailsExchange()).with(RABBITMQ_DESTINATION_NAME).noargs();
	}

	// <.>
	@Bean
	Queue emailsDeadLetterQueue() {
		return QueueBuilder.durable(RABBITMQ_DEAD_LETTER_DESTINATION_NAME).build();
	}

}
