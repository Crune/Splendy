package org.kh.splendy.config.aop;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SplendyInterceptor extends HandlerInterceptorAdapter {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SplendyInterceptor.class);
	
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handle) throws Exception {
		HandlerMethod hMethod = (HandlerMethod)handle;
		Method method = hMethod.getMethod();

		log.info(" » Intercept: " + hMethod.getBean());
		return true;
	}

}
