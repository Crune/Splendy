package org.kh.splendy;

import java.io.IOException;

import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kh.splendy.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserService userService;

	@RequestMapping("/user")
	public Principal user(Principal principal) {
		return principal;
	}

	@Override
	public void configure(WebSecurity web) throws Exception
	{
		web.ignoring()
			.antMatchers("/", "/css/**", "/img/**","/js/**", "/webjars/**");
	}

	/**
	 *  HttpSecurity - ExpressionInterceptUrlRegistry
	 *  
	 *  authenticated()		 인증된 사용자의 접근을 허용
	 *  permitAll()			 무조건 접근을 허용
	 *  
	 *  access(String)		 주어진 SpEL 표현식의 평가 결과가 true이면 접근을 허용
	 *  anonymous()			 익명의 사용자의 접근을 허용
	 *  
	 *  denyAll()			 무조건 접근을 허용하지 않음
	 *  fullyAuthenticated() 사용자가 완전히 인증되면 접근을 허용(기억되지 않음)
	 *  hasAnyAuthority(Str) 사용자가 주어진 권한 중 어떤 것이라도 있다면 접근을 허용
	 *  hasAnyRole(Str)		 사용자가 주어진 역할 중 어떤 것이라도 있다면 접근을 허용
	 *  hasAuthority(Str)	 사용자가 주어진 권한이 있다면 접근을 허용
	 *  hasIpAddress(Str)	 주어진 IP로부터 요청이 왔다면 접근을 허용
	 *  hasRole(String)		 사용자가 주어진 역할이 있다면 접근을 허용
	 *  not()				 다른 접근 방식의 효과를 무효화
	 *  rememberMe()		 기억하기를 통해 인증된 사용자의 접근을 허용
	 */

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.csrf().disable()
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.formLogin();
		/*
		http.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/user/**").permitAll()
				.antMatchers("/bbs/**").permitAll()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/db/**").access("hasRole('DBA') or hasIpAddress('121.66.252.155') or hasIpAddress('192.168.30.*')")
				.antMatchers("/**").authenticated()
				.anyRequest().authenticated()
				.and()
			.formLogin().loginPage("/").loginProcessingUrl("/user/login")
				.usernameParameter("username").passwordParameter("password")
				.defaultSuccessUrl("/lobby/").failureUrl("/?error=loginFailed")
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/").invalidateHttpSession(true).permitAll()
				.and()
			.httpBasic().realmName("splendy")
				.and()
			.rememberMe()
				.tokenValiditySeconds(2419200)
				.key("splendyKey")
				.and()
			.csrf().disable().authorizeRequests().antMatchers("/user/**").permitAll()
				.and()
			.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
				*/
		// @formatter:on
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService)
			.passwordEncoder(userService.passwordEncoder());
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

	private Filter csrfHeaderFilter() {
		return new OncePerRequestFilter() {

			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
				if (csrf != null) {
					Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
					String token = csrf.getToken();
					if (cookie == null || token != null && !token.equals(cookie.getValue())) {
						cookie = new Cookie("XSRF-TOKEN", token);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				}
				filterChain.doFilter(request, response);
			}

		};
	}
}
