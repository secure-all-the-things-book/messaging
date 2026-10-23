package com.example.messaging;

final class Constants {

	// <.>
	static final String RABBITMQ_DESTINATION_NAME = "emails";

	// <.>
	static final String RABBITMQ_DEAD_LETTER_DESTINATION_NAME = RABBITMQ_DESTINATION_NAME + ".dlq";

	// <.>
	static final String REQUESTS_MESSAGE_CHANNEL = "requests";

	// <.>
	static final String ACCESS_TOKEN_HEADER_NAME = "jwt";

	// <.>
	static final String AUTHENTICATION_HEADER_NAME = "authentication";

	private Constants() {
	}

}
