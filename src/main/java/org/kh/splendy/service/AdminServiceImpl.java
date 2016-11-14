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
	public List<PropInDB> readAll() throws Exception {
		List<PropInDB> list = adminMap.readAll();
		log.info("admin readAll service");
		return list;
	}

	@Override
	public void create(PropInDB prop) throws Exception {
		adminMap.create(prop);
		log.info("admin create service");
	}

	@Override
	public PropInDB read(String id) throws Exception {
		PropInDB prop = adminMap.read(id);
		log.info("admin read service");
		return prop;
	}

	@Override
	public void update(PropInDB prop) throws Exception {
		adminMap.update(prop);
		log.info("admin update service");
	}

	@Override
	public void delete(String id) throws Exception {
		adminMap.delete(id);
		log.info("admin delete service");
	}
}
