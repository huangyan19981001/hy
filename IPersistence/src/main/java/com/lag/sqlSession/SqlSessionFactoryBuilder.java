package com.lag.sqlSession;

import com.lag.config.XmlConfigBuilder;
import com.lag.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {
  public SqlSessionFactory Build(InputStream in) throws PropertyVetoException, DocumentException {
      //第一：使用dom4解析配置文件，将解析出来的内容封装到Configuration容器中
    XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder();
    /*需把configuration一直向下传递，直到传递到最终底层执行jdbc的代码中*/
    Configuration configuration = xmlConfigBuilder.parseConfig(in);
    //第二：创建sqlSessionFactory对象：工厂类--主要的作用就是生产sqlSession会话对象(作用：封装与数据库交互的一些crud方法)
            /*借助sqlSessionFactory生产sqlSession是一种工厂模式的体现*/
    DefaultSqlSessionFactory defaultSqlSessionfactory = new DefaultSqlSessionFactory(configuration);

    return defaultSqlSessionfactory;
  }

}
