package com.itheima.health.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;
//   分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean ){

        PageResult pageResult=checkGroupService.findPage(queryPageBean);

       return pageResult;

    }
    @RequestMapping("/findById")
    public Result findById(@RequestParam Integer id, HttpServletRequest request){

        CheckGroup checkGroup=checkGroupService.findById(id);
            if (checkGroup!=null) {
                return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);

            }

           return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);



    }
    //表单回显
    @RequestMapping("/findCountCheckgroupAndCheckitem")
    public List<Integer> findCountCheckgroupAndCheckitem(@RequestParam Integer id){
        List<Integer>list=checkGroupService.findCountCheckgroupAndCheckitem(id);
      return list;

    }
    //编辑保存
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[]checkitemIds ){

        try {
            checkGroupService.edit(checkGroup,checkitemIds);

        } catch (Exception e) {
           return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }

        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);

    }
// 根据id删除数据
@RequestMapping("/deleteById")
public Result deleteById(@RequestParam Integer id ){

    try {
        checkGroupService.deleteById(id);
    }catch (RuntimeException e){
        return new Result(false,e.getMessage());

    } catch (Exception e) {
        e.printStackTrace();
        return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
    }

    return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);


}
    //  添加表单
    @RequestMapping("/handleAdd")
    public Result handleAdd(@RequestBody CheckGroup checkGroup,Integer[]checkitemIds ) {

        try {
            checkGroupService.handleAdd(checkGroup,checkitemIds);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }

        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);


    }

    //查询所有检查组数据
    //查询所有
    @RequestMapping("/findAllCheckgroup")
    public Result findAll( ){

       List<CheckGroup>list=checkGroupService.findAll();
        if (list!=null&&list.size()>0) {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);



    }

    }
