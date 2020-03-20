package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.utils.POIUtils;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderSetting")
public class OrderSettingControlle {
    @Reference
    private OrderSettingService orderSettingService;

    //更新预约表数据
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){

        try {
         List<String[]> list =POIUtils.readExcel(excelFile);
            if (list!=null&&list.size()>0) {
                List<OrderSetting> orderSettingList=new ArrayList<>();
                for (String[] strings : list) {
                    OrderSetting orderSetting=new OrderSetting(new Date(strings[0]),Integer.parseInt(strings[1]));

                   orderSettingList.add(orderSetting);
                }
                orderSettingService.add(orderSettingList);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new  Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new  Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

    }
    //更新预约设置
    @RequestMapping("/reservationByMonth")
    public Result reservationByMonth(@RequestParam String date){

        try {
            //获取当月每天的预约情况
              List<Map>list= orderSettingService.reservationByMonth(date);
            return new  Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new  Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }


    }
    //预约设置
    @RequestMapping("/reservation")
    public Result reservation(@RequestBody OrderSetting orderSetting){

        try {
            orderSettingService.reservation(orderSetting);


        } catch (Exception e) {
            e.printStackTrace();
            return new  Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new  Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

    }
}
