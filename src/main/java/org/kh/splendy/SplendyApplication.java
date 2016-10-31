package org.kh.splendy;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@SpringBootApplication
public class SplendyApplication {

	/**
	 * 스프링 부트 시작점
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SplendyApplication.class, args);

		/*
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
		*/
	}

	/**
	 * MyBatis 사용을 위한 SqlSessionFactory 설정
	 * Bean으로 설정되어 있어서 다른 클래스에선 어노테이션으로 연동시키면 된다.
	 * 
	 * 동작
	 * - MyBatis에서 xml로 작성된 쿼리를 사용하기 위하여 Mapper.xml 위치를 팩토리에 지정한다.
	 * 
	 * @param 	dataSource			application(.properties 혹은 .yml)에 설정되어 있는 DataSource를 받아옴
	 * @return	SqlSessionFactory	MyBatis 사용을 위한 SQL 세션 팩토리 생성
	 */
	@Bean 
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);

		Resource[] arrResource = new PathMatchingResourcePatternResolver()
				.getResources("classpath:mappers/*Mapper.xml");
		sqlSessionFactoryBean.setMapperLocations(arrResource);

		return sqlSessionFactoryBean.getObject();
	}
}
