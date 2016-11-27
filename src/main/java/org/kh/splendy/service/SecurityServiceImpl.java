package org.kh.splendy.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kh.splendy.config.security.CustomUserDetails;
import org.kh.splendy.mapper.UserInnerMapper;
import org.kh.splendy.mapper.UserMapper;
import org.kh.splendy.vo.UserCore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Service
@EnableTransactionManagement
public class SecurityServiceImpl implements SecurityService {
	
	@Autowired
	private UserInnerMapper innerMap;
	
	@Autowired
	private UserMapper userMap;
	
	@Override
	public CustomUserDetails loadUserByUsername(String email) {
		CustomUserDetails cus = new CustomUserDetails();
		UserCore user = userMap.selectOne(email);
		if(user != null){
			cus.setUsername(email);
			cus.setPassword(userMap.selectPW(email));
			if(user.getNotCredential() == 0){
				cus.setCredentialsNonExpired(false);
			}else if(user.getNotLocked() == 0){
				cus.setAccountNonLocked(false);
			}
			cus.setAuthorities(getAuthorities(email));
		} else { cus.setUsername(null); }
		return cus;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities(String email) {
		int id = new Integer(userMap.serchID(email));
        List<String> string_authorities = innerMap.readAuthority(id);
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String authority : string_authorities) {
             authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
	}
}