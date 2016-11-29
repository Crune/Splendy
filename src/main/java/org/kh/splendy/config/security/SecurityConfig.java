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
				.antMatchers("/socket/**", "/socket**");
	}
	/**
	 * permitAll() -> 모두 접근 가능
	 * hasAuthority("admin") -> admin만 접속 가능
	 * formLogin() -> form형식의 로그인을 사용
	 * loginPage() -> 로그인 페이지로 사용할 url, 입력하지 않을시 기본제공 url 사용
	 * usernameParameter() -> 입력한 이름의 파라미터값을 유저네임으로 사용
	 * passwordParameter() -> 입력한 이름의 파라미터값을 패스워드로 사용
	 * loginProcessingUrl() -> 로그인이 처리되는 url, 로그인 성공시 자동으로 세션 생성, 입력하지 않을시 기본제공 url사용
	 * defaultSuccessUrl() -> 로그인 성공시 이동할 url
	 * failureUrl() -> 로그인 실패시 이동할 url
	 * logoutSuccessUrl() -> 로그아웃 성공시 이동할 url
	 * logoutUrl() -> 로그아웃 페이지로 사용할 url
	 */
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
				.antMatchers("/bbs/**", "/bbs**").permitAll()
				.antMatchers("/admin/**", "/admin**").hasAuthority("admin")
				
			.anyRequest()
				.authenticated()

		.and()
			.formLogin()
				.loginPage("/")
					.usernameParameter("email").passwordParameter("password")
					.loginProcessingUrl("/user/login_suc")
					.defaultSuccessUrl("/user/login_suc")
					.failureUrl("/user/login_suc")
					.permitAll()

		.and()
			.logout()
				.logoutSuccessUrl("/");
		//@formatter:on
	}
}
