package com.lagou.mvcframework.annotations;


import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MySecurity {

    String[] value();
}
