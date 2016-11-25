package org.kh.splendy.config;

import org.kh.splendy.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/**
 * 
 * @author 진규
 *
 * Spring Security 필터 & 설정
 * 
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/**
		 * permitAll() -> 모두 접근 가능
		 * hasAuthority("admin") -> 해당 권한이 있는 유저만 접근 가능
		 */
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/js/**").permitAll()
				.antMatchers("/img/**").permitAll()
				.antMatchers("/").permitAll()
				.antMatchers("/user/**").permitAll()
				.antMatchers("/admin/**").hasAuthority("admin")
			.anyRequest().authenticated()
			.and()
		.formLogin().loginPage("/").usernameParameter("email").passwordParameter("password")
			.loginProcessingUrl("/user/login_suc").defaultSuccessUrl("/user/login_suc").permitAll()
			.and()
		.logout().logoutSuccessUrl("/")
		;
	}

}
