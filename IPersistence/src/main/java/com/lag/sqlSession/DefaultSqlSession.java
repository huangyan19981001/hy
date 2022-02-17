package com.lag.sqlSession;

import com.lag.pojo.Configuration;
import com.lag.pojo.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private  Configuration configuration;
    public DefaultSqlSession(Configuration configuration) {

        this.configuration=configuration;
    }

    public <E> List<E> selectList(String statementId, Object... params) throws SQLException, IntrospectionException,
            NoSuchFieldException, ClassNotFoundException, InvocationTargetException, IllegalAccessException,
            InstantiationException {
        //将要去完成对simpleExecutor里query方法的调用
        //simpleExecutor里的query方法是对jdbc执行代码进行了封装
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        List<Object> list = simpleExecutor.query(configuration, mappedStatement, params);
        return (List<E>) list;
    }

    public <T> T selectOne(String statementId, Object... params) throws SQLException, IntrospectionException,
            NoSuchFieldException, ClassNotFoundException, InvocationTargetException, IllegalAccessException,
            InstantiationException {
            List<Object> objects = selectList(statementId, params);
         if(objects.size()==1){
             return (T) objects.get(0);
         }else{

             throw new RuntimeException("查询结果为空或者返回结果集过多");
         }

    }

    @Override
    public int addUser(String statementId, Object... params) throws Exception {
        SimpleExecutor executor = new SimpleExecutor();
        MappedStatement mds = configuration.getMappedStatementMap().get(statementId);
        System.out.println("MappedStatementmds:"+mds);
        int i = executor.add(configuration, mds, params);
        return i;
    }

    @Override
    public int updateUser(String statementId, Object... params) throws Exception {
        SimpleExecutor executor = new SimpleExecutor();
        MappedStatement mds = configuration.getMappedStatementMap().get(statementId);
        System.out.println("MappedStatementmds:"+mds);
        int i = executor.add(configuration, mds, params);
        return i;

    }

    @Override
    public int deleteUser(String statementId,Integer i) throws Exception {
        SimpleExecutor executor = new SimpleExecutor();
        MappedStatement mds = configuration.getMappedStatementMap().get(statementId);
        System.out.println("MappedStatementmds:"+mds);
        int ii = executor.delete(configuration, mds, i);
        return i;
    }

    @Override
    //此方法是为了使用jdk动态代理来为Dao接口生成代理对象，并返回
    public <T> T getMapper(Class<?> mapperClass) {
        //写一个匿名内部类，在内部类中实现invoke方法,再生成proxyInstance代理对象，并把生成的代理对象进行返回；
        //参数一是类加载器，参数二是被代理对象需要实现的全部接口，参数三是执行处理器：用于定义方法的增强规则
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            //参数一是代理对象，参数二是被代理对象的方法，参数三被代理对象方法被调用时传入的参数数组
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //底层都还是去执行JDBC代码，根据不同情况，来调用selectList或者selectOne
                //准备参数1:statementId:sql语句的唯一标识:namespace.id=接口全限定名.方法名
                //方法名：findAll
                /*String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String statementId=className+"."+methodName;*/
                //准备参数2:params:args
                //获取被调用方法的返回值类型
              /*  System.out.println("statementIdgetmapper:"+statementId);
                System.out.println("args:"+args);*/
                /*Type genericReturnType = method.getGenericReturnType();*/
                /*System.out.println("genericReturnType:"+genericReturnType+":"+ genericReturnType.getTypeName());
                //判断是否进行了泛型类型参数化
                if(genericReturnType instanceof ParameterizedType){
                    List<Object> objects = selectList(statementId, args);
                    return objects;
                }*/


                //2022.02.14 hy
                String statementId=method.getDeclaringClass().getName()+"."+method.getName();
                //要去判断当前调用的方法是属于哪个标签
                MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
                String sqlType = mappedStatement.getSql().trim().substring(0, 6);
                System.out.println("sqlType:"+sqlType);
                if(args==null||Arrays.asList(args).isEmpty()){
                    return selectList(statementId,null);
                }else{
                    Object onlyParam= Arrays.asList(args).get(0);
                    if("insert".equalsIgnoreCase(sqlType)){
                        return addUser(statementId,onlyParam);
                    }else if ("update".equalsIgnoreCase(sqlType)){
                        return updateUser(statementId,onlyParam);
                    }else if ("delete".equalsIgnoreCase(sqlType)){
                        Integer i= (Integer) Arrays.asList(args).get(0);
                        return deleteUser(statementId,i);
                    }else if ("select".equalsIgnoreCase(sqlType)){
                        return selectOne(statementId,onlyParam);
                    }

                }
                return proxy;
            }
        });

        return (T) proxyInstance;
    }
}
