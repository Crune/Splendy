package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.vo.Role;

public interface AdminService {
	
	void create(Role role) throws Exception;
	
	Role read(int id) throws Exception;
	
	void update(Role role) throws Exception;
	
	void delete(int id) throws Exception;
	
	List<Role> readAll() throws Exception;
}
