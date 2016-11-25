package org.kh.splendy.config;

import org.kh.splendy.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	public SecurityService secuServ;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/js/**").permitAll()
				.antMatchers("/img/**").permitAll()
				.antMatchers("/user/**").permitAll()
				.antMatchers("/").permitAll()
				.antMatchers("/lobby/").permitAll()
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
