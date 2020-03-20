package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SetmealDao {

     Setmeal findSetmealById(Integer id);

    void insertSetmealIdAndcheckgroupId(@Param(value = "setmealId") Integer setmealId, @Param(value = "checkgroupId") Integer checkgroupId);
    void handleAdd(Setmeal setmeal);
    Page<Setmeal> findPage(String queryString);

    List<Integer> findById(Integer id);

    void edit(Setmeal setmeal);

    void insertSetmealAndCheckgroupAssociation(@Param(value = "setmealId") Integer setmealId,@Param(value = "checkgroupId") Integer checkgroupId);

    long selectAssouciationById(Integer id);
    void handleDelete(Integer id);
    List<Setmeal> findAll();
    String findImg(Integer id);
    void deleteSetmealAndCkgroupAssociation(Integer setmealId);
    Integer getSetealAssociationCheckGroup(Integer id);

    Integer getSetealAssociationCheckGroupAndcheckItem(Integer id);

    Integer findSetmealAndCkgroupAssociation(Integer setmealId);
}
