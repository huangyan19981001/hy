package com.lagou.mvcframework.annotations;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface  MyAutowired {

    String value() default "";
}
