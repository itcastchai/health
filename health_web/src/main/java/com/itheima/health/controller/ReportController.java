package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.ReportService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private ReportService reportService;
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        try {
            //Calendar类一年12个月指的是0~11；
            Calendar calendar =Calendar.getInstance();//获取日期实例对象

            calendar.add(calendar.MONTH,-12);//获得当前日期向前12个月
            List<String> list=new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                calendar.add(calendar.MONTH,1);//再向前一个月
                list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
            }

            Map<String,Object> map=new HashMap<>();
             //获取月份
            map.put("months",list);
                //根据日期获取累计会员统计数量
             List<Integer>memberCount=reportService.getMemberReport(list);
             map.put("memberCount",memberCount);



          return   new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS);
        }
    }

    //获取套餐对应会员人数占比
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try {
            //新建Map集合其中第一个健为"setmealNames",第二个健为setmealCount
            Map<String,Object>map=new HashMap<>();
            //新建套餐名的集合
            List<String>setmealNames=new ArrayList<>();
            map.put("setmealNames",setmealNames);

            //获取setmealCount键值对集合
            List<Map>setmealCount=new ArrayList<>();

            map.put("setmealCount",setmealCount);


            //获取所有套餐List集合
            List<Setmeal> setmealList=reportService.findAllsetmeal();

            //遍历套餐
            for (Setmeal setmeal : setmealList) {
                String name=setmeal.getName();
                 //添加套餐
                setmealNames.add(name);
                   //根据套餐id获取相应的会员数量
                Integer value=reportService.countMemberfindBySetmealId(setmeal.getId());
                //新建套餐名对应的套餐数量map集合
                Map<String,Object> setmealToMemberMap=new HashMap<>();
                setmealToMemberMap.put("value",value);
                setmealToMemberMap.put("name",name);
                //添加套餐与会员数的map集合
                setmealCount.add(setmealToMemberMap);

            }
            //获取套餐统计数成功
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }


    }

}
