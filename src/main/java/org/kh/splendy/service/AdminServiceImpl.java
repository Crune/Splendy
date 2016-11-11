package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.mapper.AdminMapper;
import org.kh.splendy.vo.PropInDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Service
@EnableTransactionManagement
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminMapper adminMap;

	private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);
	
	@Override
	public List<PropInDB> serchServ() throws Exception {
		List<PropInDB> list = adminMap.serchServ();
		log.info("admin serch service");
		return list;
	}

	@Override
	public void servOn(String key) throws Exception {
		adminMap.servOn(key);
		log.info("admin service on");
	}

	@Override
	public void servOff(String key) throws Exception {
		adminMap.servOff(key);
		log.info("admin service off");
	}
	
}
