package com.lag.test;

import com.lag.dao.IUserDao;
import com.lag.dao.UserDaoImpl;
import com.lag.io.Resources;
import com.lag.pojo.User;
import com.lag.sqlSession.SqlSession;
import com.lag.sqlSession.SqlSessionFactory;
import com.lag.sqlSession.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class IPersistenceTest {

    //1.加载配置文件
    InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
    //2.根据SqlSessionFactoryBuilder来获取sqlSessionFactory
    SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    @Test
    public void selectTest() throws Exception {

        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.Build(resourceAsSteam);
        //3.根据sqlSessionFactory来生产sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //userDao此对象其实就是返回的代理对象
        IUserDao proxyUserDao= sqlSession.getMapper(IUserDao.class);
        List<User> all = proxyUserDao.findAll();
        for (User u:all) {
            System.out.println("user:"+u);
        }

      /*User user = new User();
        user.setId("1");
        user.setUserName("张三");
        IUserDao userDao = new UserDaoImpl();
        User user2 = userDao.findByCondition(user);*/

        //User user2=sqlSession.selectOne("User.selectOne",user);
        /*System.out.println("user2user2:"+user2);*/
      /* List<User> User3 = sqlSession.selectList("User.selectOne");
        for (User u:User3) {
            System.out.println("u:"+u);
        }*/



        //User byCondition = proxyUserDao.findByCondition(user);
        //System.out.println("byCondition:"+byCondition);

       /* IUserDao proxyUserDao= sqlSession.getMapper(IUserDao.class);
        List<User> all = proxyUserDao.findAll();
        System.out.println("byCondition:"+all);*/

    }

    @Test
    public void selectOneTest() throws Exception {

        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.Build(resourceAsSteam);
        //3.根据sqlSessionFactory来生产sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //userDao此对象其实就是返回的代理对象
        User user = new User();
        user.setId(10);
        user.setUserName("黄庆艳");
        IUserDao proxyUserDao= sqlSession.getMapper(IUserDao.class);
        User user1 = proxyUserDao.findByCondition(user);
        System.out.println("user:"+user1);


    }

    @Test
    public void addTest() throws Exception {

        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.Build(resourceAsSteam);
        //3.根据sqlSessionFactory来生产sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //userDao此对象其实就是返回的代理对象
        User user = new User();
        user.setId(11);
        user.setUserName("冰墩墩");
        IUserDao proxyUserDao= sqlSession.getMapper(IUserDao.class);
        int i = proxyUserDao.addUser(user);
        System.out.println("i:"+i);


    }

    @Test
    public void deleteTest() throws Exception {
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.Build(resourceAsSteam);
        //3.根据sqlSessionFactory来生产sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //userDao此对象其实就是返回的代理对象
        User user = new User();
        user.setId(10);
        user.setUserName("黄庆艳");
        IUserDao proxyUserDao= sqlSession.getMapper(IUserDao.class);
        int i = proxyUserDao.deleteUser(10);
        System.out.println("i:"+i);



    }

    @Test
    public void updateTest() throws Exception {

        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.Build(resourceAsSteam);
        //3.根据sqlSessionFactory来生产sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //userDao此对象其实就是返回的代理对象
        User user = new User();
        user.setId(11);
        user.setUserName("黄庆艳");
        IUserDao proxyUserDao= sqlSession.getMapper(IUserDao.class);
        int i= proxyUserDao.updateUser(user);
        System.out.println("i:"+i);

    }




  }
