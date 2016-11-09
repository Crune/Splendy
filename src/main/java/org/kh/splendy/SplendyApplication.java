package org.kh.splendy;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@SpringBootApplication
@MapperScan(basePackages = { "org.kh.splendy.mapper" })
public class SplendyApplication {

/*

자주 사용되는 애노테이션 목록 (암기!)

	클래스
	
	@Controller			스프링 MVC의 컨트롤러 객체임을 명시하는 애노테이션
	@Service			서비스 객체
	@Repository			DAO 객체
	@SessionAttributes	세션상에서 모델의 정보를 유지하고 싶은 경우에 사용
	
	---
	
	메소드	(추가로 사용되는 용도 - C:클래스, P:파라미터, R:리턴 타입)
	
	@InitBinder			피라미터를 수집해서 객체로 만들 경우에 커스터마이징
	@RequestMapping	+C	특정 URI에 매칭되는 클래스나 메소드임을 명시하는 애노테이션
	@ModelAttribute	+P	자동으로 해당 객체를 뷰까지 전달하도록 만드는 애노테이션
	@ResponseBody	+R	리턴 타입이 HTTP의 응답 메시지로 전송
	
	---
	
	파라미터
	
	@RequestParam		요청(request)에서 특정한 파라미터의 값을 찾아낼 때 사용하는 애노테이션
	@RequestHeader		요청(request)에서 특정 HTTP헤더 정보를 추출할 때 사용하는 애노테이션
	@RequestBody		요청(request) 문자열이 그대로 파라미터로 전달
	@PathVariable		현재의 URI에서 원하는 정보를 추출할때 사용하는 애노테이션
	@CookieValue		현재 사용자의 쿠키가 존재하는 경우 쿠키의 이름을 이용해서 쿠키의 값을 추출
	
	@Valid				대상 객체의 확인 조건을 만족할 경우 통과 가능
	
	---
	
기타 VO객체의 검증에 사용할 Valid 애노테이션 (참고!)
	
	@AssertFalse		false 값만 통과 가능
	@AssertTrue			true 값만 통과 가능
	
	@Future				대상 날짜가 현재보다 미래일 경우만 통과 가능
	@Past				대상 날짜가 현재보다 과거일 경우만 통과 가능
	
	@NotNull			null 값이 아닐 경우만 통과 가능
	@Null				null일 겨우만 통과 가능
	
	@DecimalMax			지정된 값 이하의 실수만 통과 가능
	@DecimalMin			지정된 값 이상의 실수만 통과 가능
	+(value=?)
		
	@Digits				대상 수가 지정된 정수와 소수 자리수보다 적을 경우 통과 가능
	+(integer=?,fraction=?)
	
	@Size				문자열 또는 배열이 지정된 값 사이일 경우 통과 가능
	+(min=?, max=?)
	
	@Max				지정된 값보다 아래일 경우만 통과 가능
	@Min				지정된 값보다 이상일 경우만 통과 가능
	+(?)
	
	@Pattern			해당 정규식을 만족할 경우만 통과 가능
	+(regex=?, flag=?)
	
	공통
	+(message="?")		오류시 전달할 메시지

	출처: http://springmvc.egloos.com/509029
*/
	public static ApplicationContext ctx;

	/** 스프링 부트 시작점
	 * @param args 받지 않을 예정 */
	public static void main(String[] args) {
		ctx = SpringApplication.run(SplendyApplication.class, args);

		/*
		 * String[] beanNames = ctx.getBeanDefinitionNames();
		 * Arrays.sort(beanNames); for (String beanName : beanNames) {
		 * System.out.println(beanName); }
		 */
	}

	@Bean
	/** SqlSessionFactory 빈
	 * MyBatis 사용을 위한 SqlSessionFactory 설정 Bean으로 설정되어 있어서 다른 클래스에선 어노테이션으로
	 * 연동시키면 된다.
	 * 
	 * 동작 - MyBatis에서 xml로 작성된 쿼리를 사용하기 위하여 Mapper.xml 위치를 팩토리에 지정한다.
	 * 
	 * @param dataSource application(.properties 혹은 .yml)에 설정되어 있는 DataSource를 받아옴
	 * @return SqlSessionFactory MyBatis 사용을 위한 SQL 세션 팩토리 생성 */
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
