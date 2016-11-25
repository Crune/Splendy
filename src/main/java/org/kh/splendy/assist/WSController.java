package org.kh.splendy.assist;

import java.lang.annotation.*;

import org.springframework.stereotype.Component;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface WSController {
	String value() default "";
}
