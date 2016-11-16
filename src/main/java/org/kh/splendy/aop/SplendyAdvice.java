package org.kh.splendy.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SplendyAdvice {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(SplendyAdvice.class);

	@Around("execution(* org.kh.splendy.controller.*.*(..))")
	public Object controllerAOP(ProceedingJoinPoint pjp) throws Throwable {
		log.info(" » Request: op - "+pjp.getSignature().getDeclaringTypeName()+" / "+pjp.getSignature().getName());
		log.info(" » Request: args - "+Arrays.toString(pjp.getArgs()));
		Object rst = pjp.proceed();
		log.info(" » Request: ed - "+pjp.getSignature().getDeclaringTypeName()+" / "+pjp.getSignature().getName());
		return rst;
	}
	
	@Around("execution(* org.kh.splendy.service.*.*(..))")
	public Object serviceAOP(ProceedingJoinPoint pjp) throws Throwable {
		log.info(" » Service: op - "+pjp.getSignature().getDeclaringTypeName()+" / "+pjp.getSignature().getName());
		log.info(" » Service: args - "+Arrays.toString(pjp.getArgs()));
		Object rst = pjp.proceed();
		log.info(" » Service: ed - "+pjp.getSignature().getDeclaringTypeName()+" / "+pjp.getSignature().getName());
		return rst;
	}
}
