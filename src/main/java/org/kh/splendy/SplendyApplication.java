package org.kh.splendy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	/** 스프링 부트 시작점 @param args 받지 않을 예정 */
	public static void main(String[] args) {
		ctx = SpringApplication.run(SplendyApplication.class, args);
		
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		log.info("Add Beans: "+ Arrays.asList(beanNames));
	}
	
}
