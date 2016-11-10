package org.kh.splendy;

import org.kh.splendy.sample.chat.MyHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


/**
 * 
 * @author 진규
 * @EnableWebSocket -> webSocket을 사용하기 위해서 설정
 * 
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	/**
	 * myHandler가 호출 되었을때 myHandler()라는 Handler를 연결
	 */
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(myHandler(),"/myHandler").withSockJS();
	}

	/**
	 * myHandler()라는 Handler는 MyHandler클레스를 생성
	 */
	@Bean
	public WebSocketHandler myHandler() {
		return new MyHandler();
	}

}
