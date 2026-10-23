package com.example.api;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.DirectChannelSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Transformers;
import org.springframework.messaging.MessageChannel;

@Configuration
class EmailRequestsIntegrationFlowConfiguration {

	private static final String DESTINATION_NAME = "emails";

	@Bean
	IntegrationFlow emailRequestsIntegrationFlow(MessageChannel requests, AmqpTemplate amqpTemplate) {
		// <.>
		var outboundAmqpAdapter = Amqp //
			.outboundAdapter(amqpTemplate) //
			.exchangeName(DESTINATION_NAME) //
			.routingKey(DESTINATION_NAME);
		return IntegrationFlow //
			.from(requests) // <.>
			.transform(Transformers.toJson()) // <.>
			.handle(outboundAmqpAdapter) // <.>
			.get();
	}

	// <.>
	@Bean
	DirectChannelSpec requests() {
		return MessageChannels.direct();
	}

}
