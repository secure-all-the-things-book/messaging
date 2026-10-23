package com.example.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

@Configuration
class SecurityConfiguration {

	@Bean
	JwtAuthenticationProvider jwtAuthenticationProvider(JwtDecoder jwtDecoder) {
		return new JwtAuthenticationProvider(jwtDecoder);
	}

}
