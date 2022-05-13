package com.lagou.service;

import com.lagou.dao.AccountDao;
import com.lagou.myAnnotation.MyAutowired;
import com.lagou.myAnnotation.MyService;
import com.lagou.myAnnotation.MyTransaction;
import com.lagou.pojo.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@MyService
@MyTransaction
public class TransferServiceImpl implements TransferService{

    //@Autowired 按照类型注入，如果按照类型无法唯一锁定对象，可以结合@Qualifier指定具体的id
    @MyAutowired
    private  AccountDao accountDao;
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {
        //事务的控制归根结底是连接Connection的控制,开启，提交，回滚
            //开启事务(关闭事务的自动提交)
         /* try {*/
             /* transactionManager.beginTransaction();*/
              System.out.println("执行转账业务逻辑Workanno");
              Account accountFrom = accountDao.queryAccountByCardNo(fromCardNo);
              Account accountTo = accountDao.queryAccountByCardNo(toCardNo);
              accountFrom.setMoney(accountFrom.getMoney()-money);
              accountTo.setMoney(accountTo.getMoney()+money);
              accountDao.updateAccountByCardNo(accountTo);
              int number=1/0;
              accountDao.updateAccountByCardNo(accountFrom);
              //提交事务/*transactionManager.commit();*/

        /*  }catch (Exception e){
              throw e;

          }*/

    }
}
