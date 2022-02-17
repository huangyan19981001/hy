package com.lag.sqlSession;

import com.lag.config.BoundSql;
import com.lag.pojo.Configuration;
import com.lag.pojo.MappedStatement;
import com.lag.utils.GenericTokenParser;
import com.lag.utils.ParameterMapping;
import com.lag.utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor{                                             //如果传入的是User
                                                                                             //Object... params可变参其实就是一个数组
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException,
            ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException,
            InvocationTargetException {
        //1.注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();

        //2.获取sql语句：select * from user where id=#{} and username=#{}
        /*因#{}占位符是自己定义的jdbc无法识别，jdbc只能识别?这样的占位符，故需转换sql语句为:select * from user where id=? and username=?
        并且在转换的过程中还需对#{}里面的值进行解析存储*/
        String sql = mappedStatement.getSql();
        System.out.println("sql:"+sql);
        BoundSql boundSql = getBoundSql(sql);;
        System.out.println("boundSql:"+boundSql);

        //3.获取预处理对象：PreparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        //4.设置参数
        //利用反射技术
        //1.获取到参数的全路径
        String paramType = mappedStatement.getParamType();
        //2.再变成Class对象
        Class<?> parameterTypeClass=getClassType(paramType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        System.out.println("parameterMappingListparameterMappingList:"+parameterMappingList);
        for(int i=0;i<parameterMappingList.size();i++){
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            System.out.println("parameterMappingparameterMapping:"+parameterMapping);
            String content = parameterMapping.getContent();
            //3.根据Class对象来获取到某一个属性对象
            Field declaredField = parameterTypeClass.getDeclaredField(content);
            //4.再获取属性对象里面的值
            //5.设置暴力访问
            declaredField.setAccessible(true);
            //o代表的就是当前declaredField属性的值
            Object o = declaredField.get(params[0]);//params[0]就是User
            preparedStatement.setObject(i+1,o);
        }
        
        //5.执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("resultSet:"+resultSet);
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);
        ArrayList<Object> objectsList = new ArrayList<Object>();
        //封装返回结果集
        while (resultSet.next()){
            Object o = resultTypeClass.newInstance();
            //获取元数据：包含了查询结果中字段的名称
            ResultSetMetaData metaData = resultSet.getMetaData();
            //metaData.getColumnCount()是查询结果中列的个数
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                System.out.println("metaData.getColumnCount():"+metaData.getColumnCount());
                //字段名
                String columnName = metaData.getColumnName(i);
                System.out.println("columnClassName:"+columnName);
                //字段值
                Object value = resultSet.getObject(columnName);
                //使用反射或者内省，根据数据库表和实体的对应关系，完成封装
                //PropertyDescriptor为内省库中的一个类，利用该类的有参构造方法创建了一个对象，
                //该对象在创建的过程中会对resultTypeClass这个类中的columnClassName这个属性来生成读写方法
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName,resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                //invoke方法是将具体的值value封装到o这个对象中
                writeMethod.invoke(o, value);
            }
            objectsList.add(o);

        }
        return (List<E>) objectsList;
    }

    @Override
    public int add(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        //1.注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();

        //2.获取sql语句：insert INTO `user`(id,userName) VALUES(#{id},#{userName})
        /*因#{}占位符是自己定义的jdbc无法识别，jdbc只能识别?这样的占位符，故需转换sql语句为:?占位符
        并且在转换的过程中还需对#{}里面的值进行解析存储*/
        String sql = mappedStatement.getSql();
        System.out.println("sql:"+sql);
        BoundSql boundSql = getBoundSql(sql);;
        System.out.println("boundSql:"+boundSql);

        //3.获取预处理对象：PreparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        //4.设置参数
        //利用反射技术
        //1.获取到参数的全路径
        String paramType = mappedStatement.getParamType();
        //2.再变成Class对象
        Class<?> parameterTypeClass=getClassType(paramType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        System.out.println("parameterMappingListparameterMappingList:"+parameterMappingList);
        for(int i=0;i<parameterMappingList.size();i++){
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            System.out.println("parameterMappingparameterMapping:"+parameterMapping);
            String content = parameterMapping.getContent();
            //3.根据Class对象来获取到某一个属性对象
            Field declaredField = parameterTypeClass.getDeclaredField(content);
            //4.再获取属性对象里面的值
            //5.设置暴力访问
            declaredField.setAccessible(true);
            //o代表的就是当前declaredField属性的值
            Object o = declaredField.get(params[0]);//params[0]就是User
            preparedStatement.setObject(i+1,o);
        }
        //5.执行sql
        int addUser= preparedStatement.executeUpdate();
        System.out.println("addUser:"+addUser);
        return addUser;
    }

    @Override
    public int update(Configuration configuration, MappedStatement mappedStatement, Object... params) {
        return 0;
    }

    @Override
    public int delete(Configuration configuration, MappedStatement mappedStatement, Integer i) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {

        //1.注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();

        //2.获取sql语句：insert INTO `user`(id,userName) VALUES(#{id},#{userName})
        /*因#{}占位符是自己定义的jdbc无法识别，jdbc只能识别?这样的占位符，故需转换sql语句为:?占位符
        并且在转换的过程中还需对#{}里面的值进行解析存储*/
        String sql = mappedStatement.getSql();
        System.out.println("sql:"+sql);
        BoundSql boundSql = getBoundSql(sql);;
        System.out.println("boundSql:"+boundSql);

        //3.获取预处理对象：PreparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        System.out.println("boundSql.getSqlText():"+boundSql.getSqlText());

        System.out.println("params:"+i);
        preparedStatement.setObject(1,i);
        //5.执行sql
        int deleteUser= preparedStatement.executeUpdate();
        System.out.println("deleteUser:"+deleteUser);
        return deleteUser;

    }

    /*完成对#{}的解析工作：1：将#{}使用?进行代替
    *                     2：解析出#{}里面的值进行存储
    * */
    private BoundSql getBoundSql(String sql) {
      /*标记处理类：配置标记解析器类来完成对#{}占位符的解析处理工作*/
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String parseSql = genericTokenParser.parse(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        System.out.println("parameterMappings"+parameterMappings);
        BoundSql boundSql = new BoundSql(parseSql, parameterMappings);
      return boundSql;
    }
    /*根据某一个类的全路径来获取到它的Class对象*/
    private Class<?> getClassType(String paramType) throws ClassNotFoundException {
      if(paramType!=null){
          Class<?> aClass = Class.forName(paramType);
       return aClass;
      }
     return null;
    }
}
