package com.lag.sqlSession;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface SqlSession {


    //查询所有
    public <E>List<E>  selectList(String statementId,Object...params) throws SQLException,
            IntrospectionException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException,
            IllegalAccessException, InstantiationException;
    //根据条件查询单个
    public <T> T selectOne(String statementId,Object...params) throws Exception;
    public int addUser(String statementId,Object...params) throws Exception;
    public int updateUser(String statementId,Object...params) throws Exception;
    public int deleteUser(String statementId,Integer i) throws Exception;

    //为Dao接口生成代理实现类
    public <T> T getMapper(Class<?> mapperClass);



}
