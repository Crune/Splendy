package org.kh.splendy.controller;

import org.kh.splendy.SampleMapper;
import org.kh.splendy.vo.TestVO;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "org.kh.splendy" })
@RestController
public class RestService {

	@Autowired
	private SampleMapper demoMapper;


	@RequestMapping(value = "/test/{name}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody TestVO test(@PathVariable String name) {
		TestVO vo = null;
		try {
			vo = demoMapper.get(name);
			/*
			log.info("result:\t"+vo);
			vo.setName("161028");
			demoMapper.update(vo);
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
}
