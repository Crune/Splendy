package org.kh.splendy.assist;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WSReqeust {
	String value() default "";
}
