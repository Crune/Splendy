package org.kh.splendy.config;

import java.util.concurrent.Executor;

import org.kh.splendy.SplendyAsyncUEHandler;
import org.kh.splendy.SplendyHandler;
import org.kh.splendy.paint.PaintHandler;
import org.kh.splendy.sample.chat.MyHandler;
import org.kh.websocket.handler.GameHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS()
		        .setStreamBytesLimit(512 * 1024)
		        .setHttpMessageCacheSize(1000)
		        .setDisconnectDelay(30 * 1000);
    }

}
/*
@Configuration
@EnableWebSocket
@EnableAsync
public class WebSocketConfig implements WebSocketConfigurer, AsyncConfigurer  {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(splendyHandler(), "/ws").withSockJS()
	        .setStreamBytesLimit(512 * 1024)
	        .setHttpMessageCacheSize(1000)
	        .setDisconnectDelay(30 * 1000);
		
		registry.addHandler(myHandler(),"/myHandler").withSockJS();
		registry.addHandler(paintHandler(),"/paint").withSockJS();
		registry.addHandler(gameHandler(), "/gameMain").withSockJS();
	}

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
    
    
	@Bean @Async
	public WebSocketHandler myHandler() {
		return new MyHandler();
	}
	
	@Bean @Async
	public WebSocketHandler paintHandler(){
		return new PaintHandler();
	}
	
	@Bean @Async
	public WebSocketHandler gameHandler(){
		return new GameHandler();
	}
	
	@Bean @Async
	public WebSocketHandler splendyHandler(){
		return new SplendyHandler();
	}

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(7);
        executor.setMaxPoolSize(42);
        executor.setQueueCapacity(11);
        executor.setThreadNamePrefix("MyExecutor-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SplendyAsyncUEHandler();
    }

}
*/