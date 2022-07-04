package com.lagou.demo.controller;

import com.lagou.demo.service.DemoService;
import com.lagou.mvcframework.annotations.MyAutowired;
import com.lagou.mvcframework.annotations.MyController;
import com.lagou.mvcframework.annotations.MyRequestMapping;
import com.lagou.mvcframework.annotations.MySecurity;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MyController
@MyRequestMapping("/demo")
public class DemoController {

    @MyAutowired
    private DemoService demoService;

    @MyRequestMapping("/handle01")
    @MySecurity({"张三"})
    public String queryZhangSan(HttpServletRequest request, HttpServletResponse response,String username){
        if(username==null){
            System.out.println("username==null");
            return "123";
        }else {
            System.out.println("张三:"+username);
            return demoService.get(username);
        }
    }

    @MyRequestMapping("/handle02")
    @MySecurity({"李四"})
    public String queryLiSi(String username){
        System.out.println("李四:"+username);
        return demoService.get(username);
    }

    @MyRequestMapping("/handle03")
    @MySecurity({"王麻子"})
    public String queryWangMaZi(String username){
        System.out.println("王麻子:"+username);
        return demoService.get(username);
    }

    @MyRequestMapping("/handle04")
    public String all(String username){
        System.out.println("/handle04:"+username);
        return demoService.get(username);
    }

}
