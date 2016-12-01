package org.kh.splendy;

import org.kh.splendy.service.CompService;
import org.kh.splendy.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan
public class SplendyApplication {

	public static ApplicationContext ctx;
	
	private static final Logger log = LoggerFactory.getLogger(SplendyApplication.class);

	@Autowired
	private CompService compService;

	/** 스프링 부트 시작점 @param args 받지 않을 예정 */
	public static void main(String[] args) {
		ctx = SpringApplication.run(SplendyApplication.class, args);

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		log.info("Add Beans: "+ Arrays.asList(beanNames));
	}
	
}
