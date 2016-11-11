package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.vo.PropInDB;

public interface AdminService {
	
	List<PropInDB> serchServ() throws Exception;
	
	void servOn(String key) throws Exception;
	
	void servOff(String key) throws Exception;
}
