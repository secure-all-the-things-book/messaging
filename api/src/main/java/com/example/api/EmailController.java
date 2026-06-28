package com.example.api;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/email")
    Map<String, Object> email(
            Principal principal,
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam Integer customerId) {
        var userName = principal.getName();
        var token = jwt.getTokenValue();
        var message = MessageBuilder
                .withPayload(userName)
                .setHeader("jwt", token)
                .build();
        var sent = this.requests.send(message);
        return Map.of("sent", sent, "customerId", customerId);
    }
}
