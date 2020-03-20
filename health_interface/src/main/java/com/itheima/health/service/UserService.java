package com.itheima.health.service;

import com.itheima.health.pojo.User;

public interface UserService {
    User findByUsername(String username);


    String findName(String username);

    void updatePassword(String username, String newPasswordCode);
}
