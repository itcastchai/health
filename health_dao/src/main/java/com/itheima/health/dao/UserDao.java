package com.itheima.health.dao;

import com.itheima.health.pojo.User;

import java.util.Map;

public interface UserDao {
    User findByUsername(String username);

    String findName(String username);

    void updatePassword(Map<String, String> map);
}
