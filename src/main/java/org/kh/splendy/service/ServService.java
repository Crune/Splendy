package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.vo.PropInDB;

public interface ServService {

	List<PropInDB> readAll() throws Exception;
	
	void create(PropInDB prop) throws Exception;
	
	PropInDB read(String id) throws Exception;
	
	void update(PropInDB prop) throws Exception;
	
	void delete(String id) throws Exception;
	
}
