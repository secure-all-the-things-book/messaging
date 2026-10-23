package com.example.api;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Map;

@Controller
@ResponseBody
class EmailController {

	private final MessageChannel requests;

	EmailController(MessageChannel requests) {
		this.requests = requests;
	}

	@GetMapping("/")
	Map<String, Object> home(Principal principal,
			@RegisteredOAuth2AuthorizedClient("crm") OAuth2AuthorizedClient auth2AuthorizedClient) {
		var jwt = auth2AuthorizedClient.getAccessToken().getTokenValue();
		var message = MessageBuilder.withPayload(principal.getName()).setHeader("jwt", jwt).build();
		var sent = this.requests.send(message);
		return Map.of("sent", sent);
	}

}
