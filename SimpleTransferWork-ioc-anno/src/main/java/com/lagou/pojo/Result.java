package com.lagou.pojo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//@Lazy此注解与xml中配置的lazy-init属性功能一样
public class Result implements BeanNameAware, BeanFactoryAware,
        ApplicationContextAware, InitializingBean, DisposableBean {

    private String status;
    private String message;


    @Override
    public String toString() {
        return "Result{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public void setBeanName(String id) {
        System.out.println("注册我成为bean时定义的id:"+id);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("管理我的beanfactory为："+beanFactory);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("高级容器接口ApplicationContext："+applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet.....");
    }

    public void initMethod(){
        System.out.println("init-method......");
    }

    @PostConstruct //此注解意为创建之后实例化之后，与xml<bean>中的init-method属性作用等值
    public void postConstruct(){
        System.out.println("postConstruct.....");
    }

    @PreDestroy //此注解与xml<bean>中的destroy-method属性作用等值
    public void preDestroy(){
        System.out.println("preDestroy...");
    }


    @Override //也是作销毁处理用的
    public void destroy() throws Exception {
        System.out.println("destroy....");
    }
}
