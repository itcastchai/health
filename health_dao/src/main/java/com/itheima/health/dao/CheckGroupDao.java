package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckGroupDao {

    List<CheckGroup> findAll();

    Page<CheckGroup> findPage(String queryString);
    CheckGroup findById(Integer id);
    List<Integer> findCountCheckgroupAndCheckitem(Integer id);
    void insert(@Param(value = "id")Integer id,   @Param(value = "checkitemId")Integer checkitemId);
    void update(CheckGroup checkGroup);
    void deleteById(Integer id);
    long findAssociationById(Integer id);
    void handleAdd(CheckGroup checkGroup);
    void deleteAssociation(Integer id);
    void insertCheckgroupAndCheckitem(@Param(value = "checkGroupId")Integer checkGroupId,@Param(value = "checkitemId") Integer checkitemId);
   List<CheckGroup>findByIdCheckGroup(Integer id);

   /* Integer findBySetealAssociationCheckGroup(Integer id);*/
}
