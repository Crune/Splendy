package org.kh.splendy.vo;

import java.lang.reflect.Method;

import lombok.Data;

@Data
public class SplendyTask {
	private Object obj;
	private Method method;
	private int repeat;
}
