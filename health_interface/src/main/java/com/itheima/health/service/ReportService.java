package com.itheima.health.service;

import com.itheima.health.pojo.Setmeal;

import java.util.List;

public interface ReportService {
    List<Integer> getMemberReport(List<String> list);
    List<Setmeal> findAllsetmeal();
    Integer countMemberfindBySetmealId(Integer id);
}
