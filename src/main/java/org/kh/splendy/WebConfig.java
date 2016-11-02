package org.kh.splendy;

import org.kh.splendy.sample.chat.MyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * @EnableWebSocket -> webSocket을 사용하기 위해서 설정
 */
@Configuration
@EnableWebSocket
public class WebConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	private static final String GoogleClientID = "768530434374-au3vrrllnhr3a96h3i6utec28filmqcn.apps.googleusercontent.com";
    private static final String GoogleClientSecret = "VR7RGj1x7ET3dG8eMUlMn_jj";
    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/");
        }
        if (!registry.hasMappingForPattern("/**")) {
            String RESOURCE_LOCATIONS = "/";
			registry.addResourceHandler("/**").addResourceLocations(
                    RESOURCE_LOCATIONS );
        }
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");
		registry.addResourceHandler("/img/**").addResourceLocations("/img/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		super.addResourceHandlers(registry);
	}
	
	/**
	 * myHandler가 호출 되었을때 myHandler()라는 Handler를 연결
	 */
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(myHandler(), "/myHandler").addInterceptors(new HttpSessionHandshakeInterceptor());
	}
	/**
	 * myHandler()라는 Handler는 MyHandler클레스를 생성
	 */
	@Bean
	public WebSocketHandler myHandler() {
		return new MyHandler();
	}

    @Bean
    public GoogleConnectionFactory getGoogleConnectionFactory() {
        GoogleConnectionFactory connectionFactory = 
                new GoogleConnectionFactory(GoogleClientID, GoogleClientSecret);
        return connectionFactory;
    }
   
   /* @Bean
    public OAuth2Parameters googleOAuth2Parameters() {
    
    }*/
    

}
