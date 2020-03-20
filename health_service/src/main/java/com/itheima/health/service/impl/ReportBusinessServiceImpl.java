package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.ReportBusinessService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportBusinessService.class)
@Transactional
public class ReportBusinessServiceImpl implements ReportBusinessService {
     @Autowired
      private MemberDao memberDao;
     @Autowired
     private OrderDao orderDao;


    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
       Map<String,Object> map= new HashMap<>();

        //一、获取日期
        //1.获取单前日期
        String  today= DateUtils.parseDate2String(new Date());
        //2.获取本周一

        String  firstWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());

        //3.获取本周日
        String  lastWeekMonday=DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        //4.获取本月第一天
        String  firstMonthDay=DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        //5.获取本月最后一天
        String lastMonthDay=DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());

       //6.封装本周日期
        Map<String,String> map1=new HashMap<>();
        map1.put("firstWeekMonday",firstWeekMonday);
        map1.put("lastWeekMonday",lastWeekMonday);
        //7.封装本月日期
        Map<String,String> map2=new HashMap<>();
        map2.put("firstMonthDay",firstMonthDay);
        map2.put("lastMonthDay",lastMonthDay);


      //二、封装map集合
      //1.当前日期
        String reportDate=today;
        map.put("reportDate",reportDate);
      //2.今天新增会员数
        Integer todayNewMember =memberDao.findTodayNewMember(reportDate);
                map.put("todayNewMember",todayNewMember);


        //3.总会员数
        Integer totalMember=memberDao.findTotalMember();
                map.put("totalMember",totalMember);


       //4.本周新增会员数(>=本周的周一的日期)
        Integer thisWeekNewMember=memberDao.findThisWeekNewMember(firstWeekMonday);
                map.put("thisWeekNewMember",thisWeekNewMember);
       //5.本月新增会员数(>=本月的第一天的日期)
        Integer thisMonthNewMember=memberDao.findThisMonthNewMember(firstMonthDay);
                map.put("thisMonthNewMember",thisMonthNewMember);
       //6.今日预约数
        Integer todayOrderNumber=todayNewMember;
                map.put("todayOrderNumber",todayOrderNumber);

       //7.今日到诊数
        Integer todayVisitsNumber=orderDao.findTodayVisitsNumber(reportDate);
                map.put("todayVisitsNumber",todayVisitsNumber);

       //8.本周预约数(>=本周的周一的日期 <=本周的周日的日期)

        Integer thisWeekOrderNumber=orderDao.findThisWeekOrderNumber(map1);
                map.put("thisWeekOrderNumber",thisWeekOrderNumber);
       //9.本周到诊数
        Integer thisWeekVisitsNumber=orderDao.findThisWeekVisitsNumber(map1);
                map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
       //10.本月预约数(>=每月的第一天的日期 <=每月的最后一天的日期)

        Integer thisMonthOrderNumber=orderDao.findThisMonthOrderNumber(map2);
                map.put("thisMonthOrderNumber",thisMonthOrderNumber);
       //11.本月到诊数
        Integer thisMonthVisitsNumber=orderDao.findThisMonthVisitsNumber(map2);
                map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
       //12.热门套餐
        List<Map>hotSetmeal=orderDao.findHotSetmeal();
             map.put("hotSetmeal",hotSetmeal);


        return map;
    }
}
