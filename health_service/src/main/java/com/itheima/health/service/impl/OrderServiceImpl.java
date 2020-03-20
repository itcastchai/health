package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass =OrderService.class )
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private SetmealDao setmealDao;
    private String orderDate;

    @Override
    public Result oder(Map map) throws Exception {
        //1.获取当前预约日期
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        //2.根据当前预约日期查看预约设置表信息
        OrderSetting orderSetting = orderSettingDao.orderSettingByOrderDate(date);
        if (orderSetting == null) {
            //没有该预约信息，不能预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //3.计算当前预约设置数据中预约人数与可预约人数对比
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (reservations >= number) {
            //预约人数已满
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //如果是会员，防止重复预约（一个会员、一个时间、一个套餐不能重复，否则是重复预约）
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByMemberTelephone(telephone);
        if (member != null) {
            //是会员，获取会员id
            Integer memberId = member.getId();
            //获取套餐Id
            Integer setmealId = Integer.parseInt((String) map.get("setmealId"));
            //在体检预约信息order中会员号
            Order order = new Order(memberId, date, null, null, setmealId);
            List<Order> orderList = orderDao.findByOrder(order);
            if (orderList != null && orderList.size() > 0) {
                //已经预约，不能重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);

            }

        }

        //修改保存预约设置表
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        //更新预约设置表
       orderSettingDao.editOrderSetting(orderSetting);
        //没有预约且不是会员，会自动添加会员
        Member member1=new Member();
        if (member==null){
           //当前前用户不是会员需要添加会员表
           member1.setName((String) map.get("name"));
           member1.setIdCard((String) map.get("idCard"));
           member1.setPhoneNumber((String) map.get("telephone"));
           member1.setSex((String) map.get("sex"));
           member1.setRegTime(new Date());
           //添加会员表
           memberDao.saveMember(member1);
       }


        //保存信息到预约表


            Order order = new Order(member1.getId(), date, (String) map.get("orderType"), Order.ORDERSTATUS_NO, Integer.parseInt((String) map.get("setmealId")));

            orderDao.addOrder(order);
            //预约成功
            return new Result(true, MessageConstant.ORDER_SUCCESS,order);

        }

    @Override
    public Map findById(Integer id) throws Exception {
        Map map=orderDao.findById(id);
        //1.根据预约id获取一条预约表数据
        if (map!=null) {
            //map不为空，格式化日期
            String orderDate= DateUtils.parseDate2String((Date) map.get("orderDate"));
            map.put("orderDate",orderDate);
        }

        return map;

    }
}
