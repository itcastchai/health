package com.itheima.health.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.utils.QiniuUtils;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @Autowired
    private JedisPool jedisPool;
    //查询所有检查组数据
    @RequestMapping("/handleAdd")
    public Result handleAdd(Integer[]checkgroupIds, @RequestBody Setmeal setmeal){
        try {
            setmealService.handleAdd(checkgroupIds,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);

    }
//分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
          PageResult pageResult=setmealService.findPage(queryPageBean);
          return pageResult;
    }

    //根据id获取套餐表的一条数据
    @RequestMapping("/findSetmealById")
    public Result findSetmealById(@RequestParam Integer id){
        try {
            Setmeal setmeal=setmealService.findSetmealById(id);
            return new Result(true,"获取指定套餐数据成功",setmeal);
        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false,"获取指定套餐数据失败");
        }

    }
    //根据套餐id通过关联表获取检查组id的集合
    @RequestMapping("/findById")
    public List<Integer> findById(@RequestParam Integer id){
        List<Integer>list=setmealService.findById(id);
        return list;
    }
    //修改套餐表数据及更新关联表数据
    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal ,Integer[] checkgroupIds){
        try {
            setmealService.edit(setmeal,checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
           return new Result(false,"编辑预约套餐失败");
        }
      return new Result(true,"编辑预约套餐成功");
    }
    //删除数据
    @RequestMapping("/handleDelete")
    public Result handleDelete(@RequestParam Integer id){
        try {
            setmealService.handleDelete(id);
        }catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除预约套餐失败");
        }
        return new Result(true,"删除预约套餐成功");
    }
    //图片上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam (value = "imgFile")MultipartFile imgFile){
        try {
            String filename = imgFile.getOriginalFilename();

            filename = UUID.randomUUID().toString() + filename.substring(filename.lastIndexOf("."));

            QiniuUtils.upload2Qiniu(imgFile.getBytes(),filename);
                //将图片存入redis当中

            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,filename);

            //图片上传成功
             Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, filename);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }

    }

}
