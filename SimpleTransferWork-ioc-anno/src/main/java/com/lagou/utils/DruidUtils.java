package com.lagou.utils;

import com.alibaba.druid.pool.DruidDataSource;


public class DruidUtils {


    private  static DruidDataSource druidDataSource=new DruidDataSource();
    static {
          druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
          druidDataSource.setUrl("jdbc:mysql://localhost:3306/zyd_mybatis");
          druidDataSource.setUsername("root");
          druidDataSource.setPassword("123456");
    }

    public static DruidDataSource getInstance(){

          return druidDataSource;

    }

}
