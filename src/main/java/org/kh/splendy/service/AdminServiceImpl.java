package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.mapper.AdminMapper;
import org.kh.splendy.vo.PropInDB;
import org.kh.splendy.vo.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Service
@EnableTransactionManagement
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	AdminMapper adminMap;
	
	private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Override
	public void create(Role role) throws Exception {
		adminMap.create(role);
		log.info("admin create admin : "+role.getId());
	}

	@Override
	public Role read(int id) throws Exception {
		Role role = adminMap.read(id);
		log.info("admin read info : "+id);
		return role;
	}

	@Override
	public void update(Role role) throws Exception {
		adminMap.update(role);
		log.info("admin update "+role.getId()+" info");
	}

	@Override
	public void delete(int id) throws Exception {
		adminMap.delete(id);
		log.info("admin delete : "+id);
	}

	@Override
	public List<Role> readAll() throws Exception {
		List<Role> list = adminMap.readAll();
		log.info("admin read all admin");
		return list;
	}

}
