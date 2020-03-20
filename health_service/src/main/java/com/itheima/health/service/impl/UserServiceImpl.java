package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.UserDao;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User findByUsername(String username) {

        return userDao.findByUsername(username);
    }

    @Override
    public String findName(String username) {

        return userDao.findName(username);
    }

    @Override
    public void updatePassword(String username, String newPasswordCode) {
        Map<String,String> map=new HashMap<>();
        map.put("username",username);
        map.put("newPasswordCode",newPasswordCode);
        userDao.updatePassword(map);


    }
}
