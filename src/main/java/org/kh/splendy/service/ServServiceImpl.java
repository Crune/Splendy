package org.kh.splendy.service;

import java.util.List;

import org.kh.splendy.mapper.ServMapper;
import org.kh.splendy.vo.PropInDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Service
@EnableTransactionManagement
public class ServServiceImpl implements ServService {

	@Autowired
	private ServMapper servMap;

	private static final Logger log = LoggerFactory.getLogger(ServServiceImpl.class);

	@Override
	public List<PropInDB> readAll() throws Exception {
		List<PropInDB> list = servMap.readAll();
		log.info("admin readAll service");
		return list;
	}

	@Override
	public void create(PropInDB prop) throws Exception {
		servMap.create(prop);
		log.info("admin create service : "+prop.getKey());
	}

	@Override
	public PropInDB read(String id) throws Exception {
		PropInDB prop = servMap.read(id);
		log.info("admin read service : "+id);
		return prop;
	}

	@Override
	public int update(PropInDB prop) throws Exception {
		int result = 0;
		PropInDB ser = servMap.read(prop.getKey());
		if(prop.getValue().equals(ser.getValue())) {
			result = 1;
		} else {
			servMap.update(prop);
			result = 2;
		}
		log.info("admin update service : "+prop.getKey());
		return result;
	}

	@Override
	public void delete(String id) throws Exception {
		servMap.delete(id);
		log.info("admin delete service : "+id);
	}
}
