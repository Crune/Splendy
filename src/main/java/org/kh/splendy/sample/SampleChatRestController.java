package org.kh.splendy.sample;

import org.kh.splendy.sample.chat.MyHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableTransactionManagement
@EnableWebSocket
@MapperScan(basePackages = { "org.kh.splendy.sample" })
@RestController
public class SampleChatRestController implements WebSocketConfigurer {
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(myHandler(), "/chat/myHandler").addInterceptors(new HttpSessionHandshakeInterceptor());
	}
	
	@Bean
	public WebSocketHandler myHandler() {
		return new MyHandler();
	}

}
