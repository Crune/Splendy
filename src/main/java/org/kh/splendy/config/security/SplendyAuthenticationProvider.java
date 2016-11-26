package org.kh.splendy.config.security;

import java.util.Collection;

import org.kh.splendy.aop.SplendyAdvice;
import org.kh.splendy.aop.SplendyExceptionAdvice;
import org.kh.splendy.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SplendyAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired private SecurityService secuServ;

	private static final Logger log = LoggerFactory.getLogger(SplendyAuthenticationProvider.class);
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		log.info("Start authenticationing!");
		
		String username= authentication.getName();
		String password = (String)authentication.getCredentials();
		CustomUserDetails cus;
		Collection<? extends GrantedAuthority> authorities;
		try {
			cus = secuServ.loadUserByUsername(username);
			String hashedPassword = SplendyAdvice.getEncSHA256(password);
			if(!hashedPassword.equals(cus.getPassword())) {
				throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
			}
			authorities = cus.getAuthorities();
		} catch(UsernameNotFoundException e) {
			throw new UsernameNotFoundException(e.getMessage());
        } catch(BadCredentialsException e) {
        	throw new BadCredentialsException(e.getMessage());
        } catch(Exception e) {
        	throw new RuntimeException(e.getMessage());
        }
		log.info("End authenticationing!");
		return new UsernamePasswordAuthenticationToken(cus,password,authorities);
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
