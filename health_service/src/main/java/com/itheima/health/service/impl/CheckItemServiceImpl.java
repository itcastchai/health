package com.itheima.health.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.CheckItemService ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;
import java.util.List;
import java.util.Set;
@Service(interfaceClass= CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    CheckItemDao checkItemDao;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private RedisTemplate<String, Setmeal> redisTemplate;


    @Override
    public void add(CheckItem checkItem) {

        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult queryPage(Integer currentPage, Integer pageSize, String queryString) {

        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = checkItemDao.selectQueryString(queryString);

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public CheckItem findById(Integer id) {

            //就从数据库中查询一条
          return   checkItemDao.findById(id);



    }

//修改
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
        Integer count=setmealDao.getSetealAssociationCheckGroupAndcheckItem(checkItem.getId());
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

    @Override
    public void deleteId(Integer id)throws RuntimeException{
        long count=checkItemDao.findCountDeleteId(id);
        if (count>0){
            //当前检查项被引用，不能删除
            throw new RuntimeException("当前检查项被检查组应用，不能删除");
        }
        checkItemDao.deleteId(id);

    }

    @Override
    public List<CheckItem> findAll() {
        return  checkItemDao.findAll();
    }
}
