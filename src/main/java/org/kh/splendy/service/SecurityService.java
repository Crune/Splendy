package org.kh.splendy.service;

import java.util.Collection;

import org.kh.splendy.vo.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface SecurityService extends UserDetailsService {
	
	public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
	
	public Collection<GrantedAuthority> getAuthorities(String email);

}
