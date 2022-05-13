package com.lagou.dao;

import com.lagou.myAnnotation.MyAutowired;
import com.lagou.myAnnotation.MyService;
import com.lagou.pojo.Account;
import com.lagou.utils.ConnectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
@MyService(value = "AccountDao")
public class  AccountDaoImpl implements AccountDao{

    @MyAutowired
    private ConnectionUtils connectionUtils;
    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    public void init() {
        System.out.println("初始化方法.....");
    }

    public void destory() {
        System.out.println("销毁方法......");
    }
    @Override
    public Account queryAccountByCardNo(String cardNo) throws Exception {
        //获取连接
        /*Connection connection = DruidUtils.getInstance().getConnection();*/
        Connection connection = connectionUtils.getCurrentThreadConn();
        String sql="select * from account where cardNo=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,cardNo);
        ResultSet resultSet = preparedStatement.executeQuery();
        Account account = new Account();
        while (resultSet.next()){
            account.setCardNo(resultSet.getString("cardNo"));
            account.setName(resultSet.getString("name"));
            account.setMoney(resultSet.getInt("money"));
        }
       if(resultSet!=null){
            resultSet.close();
        }if (preparedStatement!=null){
            preparedStatement.close();
        } /*if (connection!=null){
            connection.close();
        }*/
        return account;
    }

    @Override
    public int updateAccountByCardNo(Account account) throws Exception {
        //Connection connection = DruidUtils.getInstance().getConnection();
        Connection connection = connectionUtils.getCurrentThreadConn();
        String sql="update account set money=? where cardNo=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,account.getMoney());
        preparedStatement.setObject(2,account.getCardNo());
        int i = preparedStatement.executeUpdate();
       if (preparedStatement!=null){
            preparedStatement.close();
        }/*if (connection!=null){
            connection.close();
        }*/
        return i;
    }
}
