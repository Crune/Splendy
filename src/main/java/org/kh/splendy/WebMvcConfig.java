package org.kh.splendy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	private static final String GoogleClientID = "768530434374-au3vrrllnhr3a96h3i6utec28filmqcn.apps.googleusercontent.com";
	private static final String GoogleClientSecret = "VR7RGj1x7ET3dG8eMUlMn_jj";

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

	@Bean
	public GoogleConnectionFactory getGoogleConnectionFactory() {
		GoogleConnectionFactory connectionFactory = new GoogleConnectionFactory(GoogleClientID, GoogleClientSecret);
		return connectionFactory;
	}

	/*
	 * @Bean public OAuth2Parameters googleOAuth2Parameters() {
	 * 
	 * }
	 */
}
