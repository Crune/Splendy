package org.kh.splendy;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 *  외부 Tomcat을 위한 클래스
 *  부트 어플리케이션 생성시 설정하면 자동으로 작성하여 준다.
 *  
 * @author 자동
 *
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SplendyApplication.class);
	}

}
