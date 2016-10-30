package org.kh.splendy.sample;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "org.kh.splendy.sample" })
@RestController
public class SampleRestController {

	@Autowired
	private SampleMapper sampleMapper;

	@RequestMapping(
			value = "/test/{test}",
			method = RequestMethod.GET,
			produces = "application/json")
	public @ResponseBody TestVO test(@PathVariable String name) {
		TestVO vo = null;
		try {
			vo = sampleMapper.get(name);
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
	
	@RequestMapping(
			value = "/test.do",
			method = RequestMethod.POST,
			produces = "application/json")
	public @ResponseBody TestVO testdo(String name) {
		TestVO vo = null;
		try {
			vo = sampleMapper.get(name);
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
	
	@RequestMapping(
			value = "/test.min",
			method = RequestMethod.GET,
			produces = "application/json")
	public @ResponseBody TestVO testmin(String name) { //jsp형식이 아닌 다른 결과 값을 보낼 때  
													//@ResponseBody annotation을 사용해주어야 한다.
		TestVO vo = null;
		try {
			vo = sampleMapper.get(name);
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
