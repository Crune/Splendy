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
	public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException  {
		CustomUserDetails cus = new CustomUserDetails();
			cus.setUsername(email);
			cus.setPassword(userMap.selectPW(email));
			cus.setAuthorities(getAuthorities(email));
			// TODO: 진규.시큐리티: 알아서해
		return cus;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities(String email) {
		UserCore user = userMap.selectOne(email);
		int id = new Integer(user.getId());
        List<String> string_authorities = innerMap.readAuthority(id);
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String authority : string_authorities) {
             authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
	}
}