package com.example.messaging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.util.StringUtils;

class JwtAuthenticationInterceptor implements ChannelInterceptor {

	private final Log log = LogFactory.getLog(getClass());

	private final AuthenticationProvider authenticationProvider;

	private final String accessTokenHeaderName;

	private final String authenticationHeaderName;

	// <.>
	JwtAuthenticationInterceptor(AuthenticationProvider authenticationProvider, String accessTokenHeaderName,
			String authenticationHeaderName) {
		this.authenticationProvider = authenticationProvider;
		this.accessTokenHeaderName = accessTokenHeaderName;
		this.authenticationHeaderName = authenticationHeaderName;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		// <.>
		var builder = MessageBuilder.fromMessage(message).removeHeader(this.authenticationHeaderName);

		// <.>
		var token = message.getHeaders().get(this.accessTokenHeaderName, String.class);
		if (!StringUtils.hasText(token)) {
			return builder.build();
		}

		try {
			// <.>
			var authentication = this.authenticationProvider.authenticate(new BearerTokenAuthenticationToken(token));
			// <.>
			return builder.setHeader(this.authenticationHeaderName, authentication).build();
		}
		catch (AuthenticationException ex) {
			// <.>
			this.log.warn("could not authenticate the access token: " + ex.getMessage());
			return builder.build();
		}
	}

}
