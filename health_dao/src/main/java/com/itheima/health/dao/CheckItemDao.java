package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckItemDao {

    void add(CheckItem checkItem);
    Page<CheckItem> selectQueryString(String queryString);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);
    long findCountDeleteId(Integer id);
    void deleteId(Integer id);

    List<CheckItem>findAll();

    List<CheckItem>findByIdCheckItem(Integer id);

    Integer SetealAssociationCheckGroupAndCheckItem(Integer id);
}
