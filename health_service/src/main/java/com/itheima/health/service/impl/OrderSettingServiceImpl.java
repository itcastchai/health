package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;


    @Override
    public void add(List<OrderSetting> orderSettingList) {
        //遍历集合，获取日期
        for (OrderSetting orderSetting : orderSettingList) {

            //查看数据库中对应日期的预约数量
            int number=orderSettingDao.selectCount(orderSetting.getOrderDate());
            //2.通过预约数量判断表中是否更新对应预约日期
            if (number>0) {
                //3.数据库中表中已存在预约日期,此时需要修改数据库表中预约数量
                orderSettingDao.updateOrderSeetting(orderSetting);

            }else {
                //4.3.数据库中表中不存在预约日期,此时需要新增数据库表中预约数量
                  orderSettingDao.addOrderSeetting(orderSetting);

            }


        }
    }

    @Override
    public List<Map> reservationByMonth(String date) {
        String beginDate=date+"-1";
        String endDate=date+"-31";
        Map<String,Object>map=new HashMap<>();
        map.put("beginDate",beginDate);//开始日期
        map.put("endDate",endDate);  // 结束日期
        List<OrderSetting> list=orderSettingDao.reservationByMonth(map);

         List<Map>data=new ArrayList<>();
        //遍历当前月份的预约情况
        for (OrderSetting orderSetting : list) {
            Map<String,Object> map1=new HashMap<>();
            map1.put("date",orderSetting.getOrderDate().getDate()); //本月几号;
            map1.put("number",orderSetting.getNumber());//可预约数
            map1.put("reservations",orderSetting.getReservations());//当天已预约人数
            data.add(map1);
        }
        return data;

    }
   //设置数据
    @Override
    public void reservation(OrderSetting orderSetting) {
        //1.查询数据库当前预约日期设置是否为空
        int count=orderSettingDao.reservationCount(orderSetting.getOrderDate());
        if (count>0) {
            //预约日期不为空则执行修改数据
            orderSettingDao.updateReservation(orderSetting);
        }else {
//            否则执行更新保存数据
            orderSettingDao.AddReservation(orderSetting);
        }
    }
}
