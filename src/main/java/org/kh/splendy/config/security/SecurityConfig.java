package org.kh.splendy.config.security;

import org.kh.splendy.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/** Spring Security 필터 & 설정 @author 진규 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	public SecurityService secuServ;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
				.antMatchers("/webjars/**", "/css/**", "/js/**", "/img/**")
				.antMatchers("/socket/**", "/socket**")

				.antMatchers("/user/**", "/user**")
				.antMatchers("/lobby/**", "/lobby**")
				.antMatchers("/game/**", "/game**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http
			.csrf()
				.disable()
				
			.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/user/**").permitAll()
				.antMatchers("/lobby/**", "/lobby**").permitAll()
				.antMatchers("/game/**", "/game**").permitAll()
				.antMatchers("/admin/**", "/admin**").hasAuthority("admin")
				
			.anyRequest()
				.authenticated()

		.and()
			.formLogin()
				.loginPage("/")
					.usernameParameter("email").passwordParameter("password")
				.loginProcessingUrl("/user/login_suc")
				.defaultSuccessUrl("/user/login_suc")
				.permitAll()

		.and()
			.logout()
				.logoutSuccessUrl("/")

		.and()
			.requestMatchers()
				.antMatchers("/socket/**", "/socket/**")
/*
		.and()
			.addFilterBefore(new SplendySecurityTokenFilter(), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
*/
		;
		//@formatter:on
	}
}
