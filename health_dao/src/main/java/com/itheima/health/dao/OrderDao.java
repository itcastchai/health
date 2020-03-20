package com.itheima.health.dao;

import com.itheima.health.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    List<Order> findByOrder(Order order);
    void addOrder(Order order);
    Map  findById(Integer id);

    Integer findThisMonthVisitsNumber(Map<String, String> map2);

    Integer findThisMonthOrderNumber(Map<String, String> map2);

    Integer findTodayVisitsNumber(String reportDate);

    Integer findThisWeekOrderNumber(Map<String, String> map1);

    Integer findThisWeekVisitsNumber(Map<String, String>map1);

    List<Map> findHotSetmeal();
}
