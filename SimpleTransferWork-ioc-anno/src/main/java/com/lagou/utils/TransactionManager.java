package com.lagou.utils;

import com.lagou.myAnnotation.MyAutowired;
import com.lagou.myAnnotation.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/*
事务管理器类:负责手动事务的开启，提交，回滚
*  */


@MyService
public class TransactionManager {


    @MyAutowired
    private ConnectionUtils connectionUtils;
    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    //开启手动事务控制
    public void beginTransaction() throws SQLException {
        connectionUtils.getCurrentThreadConn().setAutoCommit(false);
        System.out.println("设置为手动提交！me");
    }

    //提交事务
    public void commit() throws SQLException {

        connectionUtils.getCurrentThreadConn().commit();
        System.out.println("事务提交！me");
    }

    //回滚事务
    public void rollback() throws SQLException {

        connectionUtils.getCurrentThreadConn().rollback();
        System.out.println("回滚成功！me");
    }


}
