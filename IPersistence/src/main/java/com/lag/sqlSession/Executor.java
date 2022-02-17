package com.lag.sqlSession;

import com.lag.pojo.Configuration;
import com.lag.pojo.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface Executor {


    public <E>List<E> query(Configuration configuration, MappedStatement mappedStatement,Object...params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException;
    public int add(Configuration configuration,MappedStatement mappedStatement,Object...params) throws Exception;
    public int update(Configuration configuration,MappedStatement mappedStatement,Object...params);
    public int delete(Configuration configuration,MappedStatement mappedStatement,Integer i) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException;

}
