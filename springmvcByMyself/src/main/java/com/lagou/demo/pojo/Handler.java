package com.lagou.demo.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;


@Data
@NoArgsConstructor
@AllArgsConstructor

/*
封装handler方法相关信息
* */
public class Handler {

    private  Object controller;//method.invoke(obj)
    private Method method;
    private Pattern pattern;
    private Map<String,Integer> paramIndexMapping; //参数顺序，是为了进行参数绑定，key是参数名，value代表是第几个参数 <name,2>
    private Map<String,Integer> securityValue;//存入MySecurity里的值

}
