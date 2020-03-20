package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;
import java.util.List;
import java.util.Set;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

   @Autowired
   private CheckGroupDao checkGroupDao;
   @Autowired
   private SetmealDao setmealDao;
    @Autowired
    private RedisTemplate<String, Setmeal> redisTemplate;

    //新增
    @Override
    public void handleAdd(CheckGroup checkGroup, Integer[] checkitemIds) {

        //新增CheckGroup表数据
        checkGroupDao.handleAdd(checkGroup);
        insertAssociation(checkGroup.getId(),checkitemIds);


    }
    public  void insertAssociation(Integer checkGroupId,Integer[] checkitemIds){
        //插入关联表单数据
        if (checkitemIds!=null&&checkitemIds.length>0){
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.insertCheckgroupAndCheckitem(checkGroupId,checkitemId);
            }
        }

    }


    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup>page=checkGroupDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {

    return checkGroupDao.findById(id);





    }

    @Override
    public List<Integer> findCountCheckgroupAndCheckitem(Integer id) {

        return checkGroupDao.findCountCheckgroupAndCheckitem(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {

        //删除关联表数据
        checkGroupDao.deleteAssociation(checkGroup.getId());
       //根据id修改CheckGroup表数据
        checkGroupDao.update(checkGroup);



        //更新关联表数据
        resetting(checkGroup.getId(),checkitemIds);

        //通过检查组查询与套餐表关联表修改后的结果是否有关联
        Integer count=setmealDao.getSetealAssociationCheckGroup(checkGroup.getId());
        if (count>0) {
            //清空缓存
            Set<String> keys = redisTemplate.keys("setemals*");

            if (keys!=null&&keys.size()>0) {
                redisTemplate.delete(keys);
            }
            List<Setmeal> setmealList = setmealDao.findAll();
            if (setmealList!=null&&setmealList.size()>0) {
                for (Setmeal setmeal : setmealList) {
                    redisTemplate.opsForValue().set("setemals"+setmeal.getId(),setmeal);

                }
            }

        }

    }


    public void resetting( Integer checkGroupId, Integer[] checkitemIds){

        if (checkitemIds!=null&&checkitemIds.length>0){
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.insert(checkGroupId,checkitemId);
            }
        }

    }

    @Override
    public void deleteById(Integer id) throws RuntimeException{

            long count = checkGroupDao.findAssociationById(id);



        if (count>0) {
            throw new RuntimeException("当前检查组被检查组应用，不能删除");
        }else{
            //删除一条数据
            checkGroupDao.deleteById(id);

        }

    }



    @Override
    public List<CheckGroup> findAll() {

     return checkGroupDao.findAll();

    }




}
