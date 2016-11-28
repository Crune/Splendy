package org.kh.splendy.config.ws;

import java.lang.SuppressWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableScheduling
@EnableWebSocket
@EnableWebSocketMessageBroker

public	class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(WebSocketConfig.class);

	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(8192);
		container.setMaxBinaryMessageBufferSize(8192);
		return container;
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
				.addEndpoint("/socket")
				.withSockJS()
				.setInterceptors(new SplendyWSHandshakeInterceptor());
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// @formatter:off
		registry.setApplicationDestinationPrefixes("/req")
				.enableSimpleBroker("/system", "/notice", "/room", "/player", "/comp", "/chat");
		// @formatter:on
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
		// @formatter:off
		registry.setSendTimeLimit(15 * 1000)
				.setSendBufferSizeLimit(512 * 1024)
				.setMessageSizeLimit(128 * 1024);
		// @formatter:on
	}

}