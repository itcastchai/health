package com.itheima.health.service;

import com.itheima.health.entity.Result;

import java.util.Map;

public interface OrderService {
    Result oder(Map map) throws Exception;

    Map findById(Integer id) throws Exception;
}
