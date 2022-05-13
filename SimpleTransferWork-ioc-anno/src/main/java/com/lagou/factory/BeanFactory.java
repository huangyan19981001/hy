package com.lagou.factory;

import com.alibaba.druid.util.StringUtils;
import com.lagou.myAnnotation.MyAutowired;
import com.lagou.myAnnotation.MyService;
import com.lagou.myAnnotation.MyTransaction;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    /*任务一：通过反射技术扫描com.lagou包下所有有@MyService注解的类，
              实例化对象并且存储待用(map集合)
              对外提供获取实例对象的接口(根据@MyService注解里的value值获取)
              控制反转
     *任务二：维护对象之间的依赖关系，类的对象属性上有@MyAutowired这个注解就需要注入这个对象，
              依赖注入
     *任务三：判断当前类是否有Transactional注解，若有则使用代理对象，
              aop
     * */
    private static HashMap<String,Object> map=new HashMap<>();//存储对象
    static {
        try {
        Reflections reflections = new Reflections("com.lagou");
        Set<Class<?>> classZ= reflections.getTypesAnnotatedWith(MyService.class);
        for (Class <?> clz:classZ) {
                Object obj = clz.newInstance();
                MyService myService = clz.getAnnotation(MyService.class);
                String myServiceValue = myService.value();
                if(StringUtils.isEmpty(myServiceValue)){
                    String[] objNames = clz.getName().split("\\.");
                    String name = objNames[objNames.length - 1];
                    System.out.println("name:"+name);
                    map.put(name,obj);
                }else {
                    map.put(myServiceValue,obj);
                }
        }

        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        int i=0;
         while (it.hasNext()){
             i++;
             String key = it.next().getKey();
             Object object = map.get(key);
             Class<?> clazz = object.getClass();
             Field[] fields = clazz.getDeclaredFields();
             for (Field field:fields) {
                boolean myAutowiredExist = field.isAnnotationPresent(MyAutowired.class);
                //boolean myAutowiredValue = field.getAnnotation(MyAutowired.class).value();
                if(myAutowiredExist){
                    String[] fieldNames = field.getType().getName().split("\\.");
                    String fieldName = fieldNames[fieldNames.length - 1];
                    System.out.println("fieldName:"+fieldName);
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method:methods) {
                        String name = method.getName();
                        String setName="set"+fieldName;
                        if(name.equalsIgnoreCase(setName)){
                                method.invoke(object, map.get(fieldName));
                                System.out.println("map.get(fieldName):"+map.get(fieldName));
                        }
                    }
                    
                }

            }
         }

         System.out.println("i:"+i);
         Iterator<Map.Entry<String, Object>> it1 = map.entrySet().iterator();
         while (it1.hasNext()){
             String key = it1.next().getKey();
             Object object = map.get(key);
             Class<?> clazz = object.getClass();
             boolean myTransactionFlag = clazz.isAnnotationPresent(MyTransaction.class);
             //String myTransactionValue = clazz.getAnnotation(MyTransaction.class).value();
             if (myTransactionFlag) {
                 ProxyFactory proxyFactory = (ProxyFactory) BeanFactory.getById("ProxyFactory");
                 Class<?>[] clazzInterfaces = clazz.getInterfaces();
                 if (clazzInterfaces.length > 0 && clazzInterfaces != null) {
                     object = proxyFactory.getJdkProxy(object);
                 } else {
                     object = proxyFactory.getCglibProxy(object);
                 }
                 System.out.println("objectTwo:" + object);
                 map.put(key, object);
             }
         }

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object getById(String id){
        return map.get(id);
    }
}
