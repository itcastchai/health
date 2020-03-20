package com.itheima.health.service.impl;
import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private  JedisPool jedisPool;
    @Autowired
    private RedisTemplate<String, Setmeal> redisTemplate;
    
    @Override
    public void handleAdd(Integer[] checkgroupIds, Setmeal setmeal) {
        //在Setmeal插入一条数据
        setmealDao.handleAdd(setmeal);
        //获取当前插入表中数据的id
        Integer setmealId=setmeal.getId();
        //调用方法更新关联表中的数据
        updateAssouciationCheckgroupAndSetmeal(checkgroupIds, setmealId);
        if (setmeal.getImg()!=null&&!"".equals(setmeal.getImg())) {
            //新增图片不为空则保存图片到缓存中
            //将图片名称保存到Redis
            savaPic2Redis(setmeal.getImg());
        }
        //调用common方法
        common();



       /* //更新缓存
        String s = JSONObject.toJSONString(setmeal);
        jedisPool.getResource().set("setemals" + setmealId, s);
*/




    }
    public void  updateAssouciationCheckgroupAndSetmeal(Integer[]checkgroupIds,Integer setmealId){
        if (checkgroupIds!=null&&checkgroupIds.length>0) {
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.insertSetmealIdAndcheckgroupId(setmealId,checkgroupId);

            }
        }
    }


    //将图片名称保存到Redis
    public void savaPic2Redis(String pic){
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);

    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Setmeal>page=setmealDao.findPage(queryPageBean.getQueryString());

        return new PageResult(page.getTotal(),page.getResult());
    }
//查询项目组id
    @Override
    public List<Integer> findById(Integer id) {

        List<Integer>list=new ArrayList<>();
        Setmeal setmeal=null;
        //从缓存中获取
          setmeal = redisTemplate.opsForValue().get("setemals" + id);
        if (setmeal!=null) {
            List<CheckGroup> checkGroups = setmeal.getCheckGroups();
            if (checkGroups!=null&&checkGroups.size()>0) {
                for (CheckGroup checkGroup : checkGroups) {
                    list.add(checkGroup.getId());
                }

            }
        }
        //数据库中获取
         list = setmealDao.findById(id);
        if (list!=null&&list.size()>0) {
            setmeal = setmealDao.findSetmealById(id);
            //更新缓存
            redisTemplate.opsForValue().set("setemals"+id,setmeal);
        }
        return list;
    }

    @Override
    public Setmeal findSetmealById(Integer id) {
        //1.从缓存中获取数据
        Setmeal setmeal=redisTemplate.opsForValue().get("setemals"+id);
        if (setmeal!=null) {
           return setmeal;

        }
        //缓存中没有就从数据库查
        setmeal = setmealDao.findSetmealById(id);
      if (setmeal!=null){
          //并将数据存缓存中

          redisTemplate.opsForValue().set("setemals"+id,setmeal);

      }


        return setmeal;


    }
//修改数据
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {

        //1.根据id获取套餐数据库原有图片
        String odlImg= setmealDao.findImg(setmeal.getId());
           String setmealImg=setmeal.getImg();

        //修改数据库照片
        if (setmealImg!=null&&setmealImg.length()>0){


            if (odlImg==null){

                jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmealImg);
            }else {

                if (!odlImg.equals(setmealImg)){

                    jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,odlImg);
                    jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmealImg);
                }


            }



        }

        //查询当前id对应的检查组的关系
        Integer setmealId = setmeal.getId();

        Integer count=setmealDao.findSetmealAndCkgroupAssociation(setmealId);

        if (count>0){

            //2.先删除关联表数据中的
            setmealDao.deleteSetmealAndCkgroupAssociation( setmealId );

            //3.更新关联表数据
            reSettingSetmealAndCheckgroup(setmealId,checkgroupIds);
            //4.修改数据
            setmealDao.edit(setmeal);
            //调用common方法
            common();


        }
        //调用common方法
        common();




 /*       //4.直接修改套餐表数据
        setmealDao.edit(setmeal);
        //更新缓存
        String s = JSONObject.toJSONString(setmeal);
        jedisPool.getResource().set("setemals"+setmeal.getId(),s);*/



    }

    public void reSettingSetmealAndCheckgroup(Integer setmealId,Integer[]checkgroupIds){

        if (checkgroupIds!=null&&checkgroupIds.length>0){

            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.insertSetmealAndCheckgroupAssociation(setmealId,checkgroupId);


            }
        }

    }
//删除
    @Override
    public void handleDelete(Integer id) throws RuntimeException{

        //1.查询关联表是否有相关联数据
       long count= setmealDao.selectAssouciationById(id);
        if (count>0) {
           throw new RuntimeException("有关联数据不能删除");
        }else {
            setmealDao.handleDelete(id);
          common();

            /*String s = jedisPool.getResource().get("setemals"+id);



             *//*   if (s!=null&&s.length()>0){

                //更新缓存删除该套餐
                jedisPool.getResource().del("setemals"+id);
            }*/


        }

    }
    //增删改
    public void common(){
        //清空缓存
        //更缓存中删除该套餐

        Set<String> keys = redisTemplate.keys("setemals*");
        if (keys!=null&&keys.size()>0){

            redisTemplate.delete(keys);


        }

        //从数据库中查
        List<Setmeal> setmealList = setmealDao.findAll();

        if (setmealList!=null&&setmealList.size()>0) {
            for (Setmeal setmeal : setmealList) {
                redisTemplate.opsForValue().set("setemals"+setmeal.getId(),setmeal);
            }
        }

    }

    //查询所有套餐
    @Override
    public List<Setmeal> findAll(){

        List<Setmeal>list=new ArrayList<>();

        //1.获取缓存中所有数据
        Set<String> keys = redisTemplate.keys("setemals*");
        if (keys==null||keys.size()==0){
            List<Setmeal>setmealList=setmealDao.findAll();
            for (Setmeal setmeal : setmealList) {
                redisTemplate.opsForValue().set("setemals"+setmeal.getId(),setmeal);
               setmeal = redisTemplate.opsForValue().get("setemals"+setmeal.getId());
                list.add(setmeal);

            }
        }else {
            //若缓存中不为空则从缓存中获取数据
            for (String key : keys) {

                Setmeal setmeal=  redisTemplate.opsForValue().get(key);
                list.add(setmeal);
            }
        }

        return  list;


    }




}
