package com.example.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// the issuer-uri triggers OIDC discovery at startup, which needs a running authorization
// server, so the test points the registration at a provider whose endpoints are spelled out
@SpringBootTest(properties = { "spring.security.oauth2.client.registration.crm.provider=test",
		"spring.security.oauth2.client.provider.test.authorization-uri=http://localhost:9090/oauth2/authorize",
		"spring.security.oauth2.client.provider.test.token-uri=http://localhost:9090/oauth2/token",
		"spring.security.oauth2.client.provider.test.jwk-set-uri=http://localhost:9090/oauth2/jwks",
		"spring.security.oauth2.client.provider.test.user-info-uri=http://localhost:9090/userinfo",
		"spring.security.oauth2.client.provider.test.user-name-attribute=sub" })
class ApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
