package com.lag.dao;

import com.lag.pojo.User;


/*优化自定义持久层框架*/
import java.util.List;

public interface IUserDao {

    public List<User> findAll();
    public User findByCondition(User user);
    public int addUser(User user);
    public int updateUser(User user);
    public int deleteUser(int id);


}
