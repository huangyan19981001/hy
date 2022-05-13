package com.lagou.factory;

import com.lagou.myAnnotation.MyAutowired;
import com.lagou.myAnnotation.MyService;
import com.lagou.utils.TransactionManager;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/*
代理对象工厂：生成代理对象的
* */
@MyService
public class ProxyFactory {

    @MyAutowired
    private TransactionManager transactionManager =new TransactionManager();
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public  Object getJdkProxy(Object obj){
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object result=null;
                try {
                    transactionManager.beginTransaction();
                     result = method.invoke(obj, args);
                    transactionManager.commit();
                }catch (Exception e){
                    transactionManager.rollback();
                    throw e.getCause();

                }
              return result;
            }
        });



    }
    public  Object getCglibProxy(Object obj){
       return Enhancer.create(obj.getClass(), new MethodInterceptor() {
           @Override
           public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
               Object result=null;
               try {
                   transactionManager.beginTransaction();
                   result = method.invoke(obj, objects);
                   transactionManager.commit();
               }catch (Exception e){
                   transactionManager.rollback();
                   throw e;

               }
               return result;
           }
       });
    }







}
