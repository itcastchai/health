package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
   void add(CheckItem checkItem);

    PageResult queryPage(Integer currentPage, Integer pageSize, String queryString);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    void deleteId(Integer id);

    List<CheckItem>findAll();
}
