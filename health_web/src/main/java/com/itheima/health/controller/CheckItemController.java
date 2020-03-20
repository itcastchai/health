package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;//远程调用方法



    //新增
    @PreAuthorize(value ="hasAnyAuthority('CHECKITEM_ADD')" )
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        }catch (Exception e){
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }
    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=checkItemService.queryPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());

        return pageResult;
    }
    //根据id查询数据
    @RequestMapping("/findById")
    public Result findById(@RequestParam Integer  id){
        try {
                CheckItem checkItem=checkItemService.findById(id);
                 return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }


    }
    //根据修改成数据
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
             checkItemService.edit(checkItem);
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }


    }

    //删除数据
    //权限访问
    @PreAuthorize(value = "hasAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/deleteId")
    public Result deleteId(@RequestParam Integer id){
        try {
            checkItemService.deleteId(id);
        }catch(RuntimeException e){
           return new Result(false,e.getMessage());

        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_CHECKITEM_SUCCESS );
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_FAIL);

    }
    //查询所有
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
          List<CheckItem> list =checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }

}
