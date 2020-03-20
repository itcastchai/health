package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderSettingDao {

    void updateOrderSeetting(OrderSetting orderSetting);

    void addOrderSeetting(OrderSetting orderSetting);

    int selectCount(Date orderDate);

    List<OrderSetting> reservationByMonth(Map map);

    int reservationCount(Date orderDate);

    void updateReservation(OrderSetting orderSetting);

    void AddReservation(OrderSetting orderSetting);

    void editOrderSetting(OrderSetting orderSetting);

    OrderSetting orderSettingByOrderDate(Date date);
}


