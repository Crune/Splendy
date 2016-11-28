package org.kh.splendy.config;

import org.kh.splendy.config.aop.SplendyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (!registry.hasMappingForPattern("/webjars/**")) {
			registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		}
		if (!registry.hasMappingForPattern("/**")) {
			registry.addResourceHandler("/**").addResourceLocations("/");
		}
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");
		registry.addResourceHandler("/img/**").addResourceLocations("/img/");
		super.addResourceHandlers(registry);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SplendyInterceptor());
		super.addInterceptors(registry);
	}

/*
	@Bean
	public JedisConnectionFactory connectionFactory() {
		return new JedisConnectionFactory();
	}

	private static final String GoogleClientID = "768530434374-au3vrrllnhr3a96h3i6utec28filmqcn.apps.googleusercontent.com";
	private static final String GoogleClientSecret = "VR7RGj1x7ET3dG8eMUlMn_jj";

	@Bean
	public GoogleConnectionFactory getGoogleConnectionFactory() {
		GoogleConnectionFactory connectionFactory = new GoogleConnectionFactory(GoogleClientID, GoogleClientSecret);
		return connectionFactory;
	}
*/
	/*
	 * @Bean public OAuth2Parameters googleOAuth2Parameters() {
	 * 
	 * }
	 */
}
