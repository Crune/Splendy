package org.kh.splendy;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

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
		
		String beans = "";
		Arrays.asList(beanNames).forEach(e->String.join(", ",beans, e));
		log.info("Add Beans: "+beans);
	}
	
}
