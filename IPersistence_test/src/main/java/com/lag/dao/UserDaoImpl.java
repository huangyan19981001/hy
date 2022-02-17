package com.lag.dao;

import com.lag.io.Resources;
import com.lag.pojo.User;
import com.lag.sqlSession.SqlSession;
import com.lag.sqlSession.SqlSessionFactory;
import com.lag.sqlSession.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class UserDaoImpl{
   /* public List<User> findAll() throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        System.out.println("sqlConfigResourceAsSteam"+resourceAsSteam);
        //2.根据SqlSessionFactoryBuilder来获取sqlSessionFactory
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.Build(resourceAsSteam);
        //3.根据sqlSessionFactory来生产sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<User> users =sqlSession.selectList("User.selectList");

        return users;
    }

    public User findByCondition(User user) throws Exception{
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        System.out.println("sqlConfigResourceAsSteam"+resourceAsSteam);
        //2.根据SqlSessionFactoryBuilder来获取sqlSessionFactory
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.Build(resourceAsSteam);
        //3.根据sqlSessionFactory来生产sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user2=sqlSession.selectOne("User.selectOne",user);
        System.out.println("user2user2:"+user2);

        return user2;
    }*/
}
