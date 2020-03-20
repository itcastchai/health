package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {


    void handleAdd(CheckGroup checkGroup, Integer[] checkitemIds);

    //分页查询
    PageResult findPage(QueryPageBean queryPageBean);

   CheckGroup findById(Integer id);

    List<Integer> findCountCheckgroupAndCheckitem(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    void deleteById(Integer id);

    List<CheckGroup>findAll();
}
