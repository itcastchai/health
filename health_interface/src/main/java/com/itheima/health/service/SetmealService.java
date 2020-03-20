package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.Setmeal;

import java.util.List;

public interface SetmealService {
    void handleAdd(Integer[] checkgroupIds, Setmeal setmeal);

    PageResult findPage(QueryPageBean queryPageBean);

    List<Integer> findById(Integer id);

    Setmeal findSetmealById(Integer id);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    void handleDelete(Integer id);

    List<Setmeal>findAll();

}
