package org.kh.splendy.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.kh.splendy.annotation.WSReqeust;
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

	public static void inject(Object getObj, Object setObj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method[] methods = getObj.getClass().getMethods();
		
		Map<String, Method> getter = new HashMap<String, Method>();
		Map<String, Method> setter = new HashMap<String, Method>();
		
		for (Method m : methods) {
			if (m.getName().length()>3) {
				String head = m.getName().substring(0, 3);
				String name = m.getName().substring(3);
				if (head.equals("set")) setter.put(name, m);
				if (head.equals("get")) getter.put(name, m);
			}
		}
		
		for (String curMethodName : setter.keySet()) {
			Object data = getter.get(curMethodName).invoke(getObj);
			setter.get(curMethodName).invoke(setObj,data);
		}
	}

	public static String getEncSHA256(String txt) throws NoSuchAlgorithmException {
		StringBuffer sbuf = new StringBuffer();
	
		MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
		mDigest.update(txt.getBytes());
	
		byte[] msgStr = mDigest.digest();
	
		for (int i = 0; i < msgStr.length; i++) {
			byte tmpStrByte = msgStr[i];
			String tmpEncTxt = Integer.toString((tmpStrByte & 0xff) + 0x100, 16).substring(1);
	
			sbuf.append(tmpEncTxt);
		}
	
		return sbuf.toString();
	}
}
