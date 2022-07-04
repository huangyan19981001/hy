package com.lagou.demo.service.impl;

import com.lagou.demo.service.DemoService;
import com.lagou.mvcframework.annotations.MyService;

@MyService("demoService")
public class DemoServiceImpl implements DemoService {

    @Override
    public String get(String name) {
        System.out.println("get:"+name);
        return name;
    }
}
