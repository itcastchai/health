package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.ReportDao;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl  implements ReportService {

    @Autowired
    private ReportDao reportDao;
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private MemberDao memberDao;

    @Override
    public List<Integer> getMemberReport(List<String> list) {
        List<Integer>memberCount=new ArrayList<>();

        //设置日期
        for (String  m : list) {
             m=m+"-31";
            Integer count=reportDao.getMemberReport(m);
             memberCount.add(count);
        }
        return memberCount;
    }

    @Override
    public List<Setmeal> findAllsetmeal() {
        return setmealDao.findAll();
    }

    @Override
    public Integer countMemberfindBySetmealId(Integer id) {
        //根据套餐id查询套餐数

        return memberDao.countMemberfindBySetmealId(id);
    }
}
