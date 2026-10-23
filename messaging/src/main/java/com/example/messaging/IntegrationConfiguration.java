package com.example.messaging;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.DirectChannelSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Transformers;
import org.springframework.messaging.MessageChannel;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.messaging.access.intercept.AuthorizationChannelInterceptor;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

import static com.example.messaging.Constants.ACCESS_TOKEN_HEADER_NAME;
import static com.example.messaging.Constants.AUTHENTICATION_HEADER_NAME;
import static com.example.messaging.Constants.RABBITMQ_DESTINATION_NAME;
import static com.example.messaging.Constants.REQUESTS_MESSAGE_CHANNEL;

@Configuration
class IntegrationConfiguration {

	// <.>
	@Bean
	IntegrationFlow inboundAmqpRequestsIntegrationFlow(@Qualifier(REQUESTS_MESSAGE_CHANNEL) MessageChannel requests,
			ConnectionFactory connectionFactory) {
		var inboundAmqpAdapter = Amqp.inboundAdapter(connectionFactory, RABBITMQ_DESTINATION_NAME)
			.configureContainer(container -> container.defaultRequeueRejected(false)); // <.>
		return IntegrationFlow //
			.from(inboundAmqpAdapter) //
			.channel(requests) // <.>
			.get();
	}

	// <.>
	@Bean
	IntegrationFlow requestsIntegrationFlow(@Qualifier(REQUESTS_MESSAGE_CHANNEL) MessageChannel requests) {
		return IntegrationFlow //
			.from(requests) //
			.transform(Transformers.fromJson(EmailRequest.class)) // <.>
			.handle(EmailRequest.class, (payload, _) -> {
				// <.>
				var authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
				IO.println("sending an email with subject [%s] to [%s]".formatted(payload.subject(),
						authentication.getName()));
				return null;
			}) //
			.get();
	}

	// <.>
	@Bean(REQUESTS_MESSAGE_CHANNEL)
	DirectChannelSpec requests(JwtAuthenticationProvider jwtAuthenticationProvider) {
		// <.>
		var jwtAuthenticationInterceptor = new JwtAuthenticationInterceptor(jwtAuthenticationProvider,
				ACCESS_TOKEN_HEADER_NAME, AUTHENTICATION_HEADER_NAME);
		// <.>
		var securityContextChannelInterceptor = new SecurityContextChannelInterceptor(AUTHENTICATION_HEADER_NAME);
		// <.>
		var authorizationChannelInterceptor = new AuthorizationChannelInterceptor(
				AuthorityAuthorizationManager.hasAuthority("SCOPE_email.send"));
		return MessageChannels //
			.direct() //
			.interceptor(jwtAuthenticationInterceptor, securityContextChannelInterceptor,
					authorizationChannelInterceptor);
	}

}
