package com.lagou.mvcframework.servlet;

import com.lagou.demo.pojo.Handler;
import com.lagou.mvcframework.annotations.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DispatcherServletMyself extends HttpServlet {

   private Properties properties=new Properties();
   private Reflections reflections =null;
   private HashMap<String,Object> hashMap=new HashMap<>();
   private List<Handler> handlerList=new ArrayList<>();


    @Override
    public void init(ServletConfig config) throws ServletException {
         /* 1 加载配置文件 springmvc.properties
         *  2 扫描相关的类，扫描注解
         *  3 初始化bean对象(实现ioc容器，基于注解)
         *  4 实现依赖注入
         *  5 构造一个HandlerMapping处理器映射器，将配置好的url和Method建立映射关系
         *  完成这5步：初始化完成，等待请求进入，处理请求
         * */
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        doLoadConfig(contextConfigLocation);
        String scanPackage = properties.getProperty("scanPackage");
        doScan(scanPackage);
        doInstance();
        initHandlerMapping();
        doAutowired();
         System.out.println("lagou mvc 初始化完成.....");
        //等待请求进入，处理请求 到doPost();
    }
    private void doLoadConfig(String contextConfigLocation) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //scanPackage:com.lagou.demo package--->磁盘上的文件夹(File) com/lagou/demo
    private void doScan(String scanPackage) {
         reflections = new Reflections(scanPackage);
    }

    private void doInstance() {
        Set<Class<?>> myControllerClass = reflections.getTypesAnnotatedWith(MyController.class);
        try {
         for (Class<?> ControllerClass:myControllerClass) {
                String simpleName = ControllerClass.getSimpleName();
                String lowerSimpleName = lowerFirst(simpleName);
                Object obj = ControllerClass.newInstance();
                hashMap.put(lowerSimpleName,obj);
           
        }
        Set<Class<?>> myServiceClass = reflections.getTypesAnnotatedWith(MyService.class);
        for (Class<?> ServiceClass:myServiceClass) {
            Object o = ServiceClass.newInstance();
            System.out.println("o:"+o);
            MyService myService = ServiceClass.getAnnotation(MyService.class);
            String value = myService.value();
            if(StringUtils.isEmpty(value)){
                String lowSimpleName = lowerFirst(ServiceClass.getSimpleName());
                hashMap.put(lowSimpleName,o);
            }else{
                String loSimpleName = lowerFirst(value);
                hashMap.put(loSimpleName,o);
             }
            //这里这样写会报java.lang.InstantiationException,因为类对象是接口不能实例化
           /* Class<?>[] interfaces = ServiceClass.getInterfaces();
            for (Class<?> clz:interfaces) {
                Object object= clz.newInstance();
                String loSimpleName = lowerFirst(clz.getSimpleName());
                hashMap.put(loSimpleName,object);
             }*/
           }
         }catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace(); }
    }

    private void doAutowired() {
        Set<Map.Entry<String, Object>> entries = hashMap.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> next = iterator.next();
            Object obj = next.getValue();
            Class<?> aClass = obj.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field field:declaredFields) {
                boolean myAutowiredFlag = field.isAnnotationPresent(MyAutowired.class);
                if(myAutowiredFlag){
                    String lSimpleName = lowerFirst(field.getType().getSimpleName());
                    Object lSimpleNameObject = hashMap.get(lSimpleName);
                    field.setAccessible(true);
                    try {
                        field.set(obj,lSimpleNameObject);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private void initHandlerMapping() {
        if(hashMap.isEmpty()){
            return;
        }
        Set<Map.Entry<String, Object>> entries = hashMap.entrySet();
        for(Map.Entry<String, Object> entry:entries){
            Class<?> aClass = entry.getValue().getClass();
            System.out.println("aClass:"+aClass);
            boolean myControllerFlag = aClass.isAnnotationPresent(MyController.class);
            if(!myControllerFlag){
                continue;
            }
            String value1 ="";
            if(aClass.isAnnotationPresent(MyRequestMapping.class)){
                MyRequestMapping myRequestMapping = aClass.getAnnotation(MyRequestMapping.class);
                 value1 = myRequestMapping.value();// /demo
            }
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method method:declaredMethods) {
                boolean MyRequestMappingFlag = method.isAnnotationPresent(MyRequestMapping.class);
                if(!MyRequestMappingFlag){
                   continue;
                }
                String value2 = method.getAnnotation(MyRequestMapping.class).value();// /query
                boolean MySecurityFlag = method.isAnnotationPresent(MySecurity.class);
                HashMap<String, Integer> securityValue = new HashMap<>();
                if(MySecurityFlag){
                    String[] mySecurityValue = method.getAnnotation(MySecurity.class).value();
                    for (int i = 0; i < mySecurityValue.length; i++) {
                        securityValue.put(mySecurityValue[i],i);
                    }
                }
                String url=value1+value2;// /demo/query
                //把method所有信息及url封装为一个Handler
                HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i <parameters.length ; i++) {
                    Class<?> type = parameters[i].getType();
                    String simpleName = parameters[i].getType().getSimpleName();
                    if(type==HttpServletRequest.class||type==HttpServletResponse.class){
                        // 如果是request和response对象，那么参数名称写HttpServletRequest和HttpServletResponse
                        stringIntegerHashMap.put(simpleName,i);
                        System.out.println("simpleName:"+simpleName);
                        System.out.println("SimpleNameI:"+i);
                    }else {
                        String name = parameters[i].getName();
                        stringIntegerHashMap.put(name,i); // <name,2>
                        System.out.println("name:"+name);
                        System.out.println("nameI:"+i);
                    }
                }
                Object objectValue = entry.getValue();
                System.out.println("objectValue:"+objectValue);
                Handler handler = new Handler(entry.getValue(),method,Pattern.compile(url),stringIntegerHashMap,securityValue);
                System.out.println("Pattern.compile(url):"+Pattern.compile(url));
                handlerList.add(handler);
            }

        }

    }

    private String lowerFirst(String simpleName) {
        char[] chars = simpleName.toCharArray();
        if(chars[0]>='A'&&chars[0]<='Z'){
            chars[0]+=32;
        }
        String lowerValue = String.valueOf(chars);
        return lowerValue;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)  {
        //处理请求：根据url，找到对应的Method方法，进行调用
        //获取url String requestURI = req.getRequestURI();
        //Method method = handlerMapping.get(requestURI);// 获取到一个反射的方法
        //如果声明的是一个hashMap存入的只有url和method那么会获取到一个反射的方法
        /*方法反射调用，需要传入对象，需要传入参数，此处无法完成调用，没有把对象缓存起来，也没有参数，故只声明一个hashMap存储是不行的，
        根据这一问题，handlerList解决如下
        */
        //method.invoke()

        Handler handler = getHandler(req);
        if(handler==null){
                resp.getWriter().write("404 not found");
            return;
        }
        //参数绑定，获取所有参数类型数组，这个数组的长度就是我们最后传入的agrs数组的长度
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
        //根据上述数组长度创建一个新的数组(参数数组，是要传入反射调用的)
        Object[] paraValues = new Object[parameterTypes.length];
        //以下就是为了向参数数组中塞值，而且还得保证参数的顺序和方法中形参顺序一致
        Map<String, String[]> parameterMap = req.getParameterMap();
        Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();
        //遍历request中所有参数，填充除了request,response之外的参数
        Map<String, Integer> paramIndexMapping = handler.getParamIndexMapping();
        Map<String, Integer> securityValue = handler.getSecurityValue();
        boolean flag=true;
        for (Map.Entry<String, String[]> param:entries) {
            String key = param.getKey();
            String[] paramValue = param.getValue(); //name=1&name=2 name[1,2]
            int paramLength = paramValue.length;
            String Value = StringUtils.join(paramValue, ",");//1,2
            //如果参数与方法中的参数匹配上了，填充数据
            if(!paramIndexMapping.containsKey(param.getKey())){
                //flag=false;
                continue;
            } else {
                for (String v :paramValue) {
                    if(securityValue.size()!=0){
                        flag= securityValue.containsKey(v);
                        if(!flag){
                            break;
                        }
                    }
                }
                if(!flag){
                }else {
                    //方法形参确实有该参数，找到它的索引位置，对应的把参数值放入paraValues
                    Integer index = handler.getParamIndexMapping().get(param.getKey());//name在第2个位置
                    //把前台传递过来的参数值填充到对应的位置去
                    paraValues[index]=Value;
                    System.out.println("index"+index);
                    System.out.println("Value"+Value);

                }
            }
        }
        if(flag){
            if(paramIndexMapping.containsKey(HttpServletRequest.class.getSimpleName())||paramIndexMapping.containsKey(HttpServletResponse.class.getSimpleName())){
                Integer integer = handler.getParamIndexMapping().get(HttpServletRequest.class.getSimpleName());//0
                Integer integer1 = handler.getParamIndexMapping().get(HttpServletResponse.class.getSimpleName());//1
                paraValues[integer]=req;
                paraValues[integer1]=resp;
            }
            //最终调用handler的method属性
            Method method = handler.getMethod();
            Object controller = handler.getController();
            method.invoke(controller,paraValues);
        }else {
            resp.getWriter().write("404");
        }

    }

    private Handler getHandler(HttpServletRequest req) {
        if(handlerList.isEmpty()){
            return null;
        }
        String requestURI = req.getRequestURI();
        for (Handler handler:handlerList) {
            Matcher matcher = handler.getPattern().matcher(requestURI);
             if(!matcher.matches()){
               continue;
            }
            return handler;
        }
       return null;
    }

}
