package org.kh.splendy.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@MapperScan(basePackages = { "org.kh.splendy.mapper" })
public class DBConfig {


	/** SqlSessionFactory
	 * MyBatis 사용을 위한 SqlSessionFactory 설정
	 * Bean으로 설정되어 있어서 다른 클래스에선 '@Autowired' 하면 된다.
	 * 
	 * 동작 - MyBatis에서 xml로 작성된 쿼리를 사용하기 위하여 Mapper.xml 위치를 팩토리에 지정한다.
	 * 
	 * @param dataSource application(.properties 혹은 .yml)에 설정되어 있는 DataSource를 받아옴
	 * @return SqlSessionFactory MyBatis 사용을 위한 SQL 세션 팩토리 생성 */
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);

		Resource[] arrResource = new PathMatchingResourcePatternResolver()
				.getResources("classpath:mappers/*Mapper.xml");
		sqlSessionFactoryBean.setMapperLocations(arrResource);

		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
