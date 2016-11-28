package org.kh.splendy.config.security;

import java.util.Collection;

import org.kh.splendy.config.aop.SplendyAdvice;
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
/**
 * 시큐리티 처리가 진행되는곳 @author jingyu
 */
@Component
public class SplendyAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired private SecurityService secuServ;

	private static final Logger log = LoggerFactory.getLogger(SplendyAuthenticationProvider.class);
	/**
	 * authentication 로그인 페이지에서 사용자가 입력한 정보
	 * CustomUserDetails -> security에서 사용하는 vo
	 */
	@Override
	public Authentication authenticate(Authentication authentication) {
		
		log.info("Start authenticationing!");
		
		String username= authentication.getName();
		String password = (String)authentication.getCredentials();
		CustomUserDetails cus;
		Collection<? extends GrantedAuthority> authorities = null;
		String hashedPassword = null;
		
		cus = secuServ.loadUserByUsername(username); // 사용자가 입력한 이메일로 데이터베이스에 저장된 정보를 불러옴
		if(cus.getUsername() != null) { // 입력한 유저네임이 존재하는지 검사
			try {
				hashedPassword = SplendyAdvice.getEncSHA256(password); // 사용자가 입력한 비밀번호를 암호화
				if(!cus.getPassword().equals(hashedPassword)){ // 데이터베이스상의 암호와 비교
					cus.setPassword(null); // 일치하지 않으면 null
				} else { authorities = cus.getAuthorities(); }
			} catch (Exception e) { e.printStackTrace(); }
		}
		log.info("End authenticationing!");
		return new UsernamePasswordAuthenticationToken(cus,password,authorities);
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
