package com.java.common.interceptor.annotation;

import java.lang.annotation.*;


@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DDOSValidate {
	boolean validate() default true;
	int second() default 1;
}
