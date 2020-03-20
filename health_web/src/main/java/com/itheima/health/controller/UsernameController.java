package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UsernameController {

    @Reference
    private UserService userService;


    //获取用户名
    @RequestMapping("/getUsername")
    public Result getUsername(){
        try {
            org.springframework.security.core.userdetails.User user= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }

    }

    @RequestMapping("/updatePassword1")
    public Result updatePassword(@RequestBody Map map){

        try {
            //获取用户名
           String username = (String) map.get("username");
            //1.获取原密码
            String password = (String) map.get("password");

            //2.根据用户名获取原加密密码
            String passwordCoder=userService.findName(username);

            //检验表单输入的原密码与数据库中的原密码是否一致
            BCryptPasswordEncoder  encoder=new BCryptPasswordEncoder();
            boolean b = encoder.matches(password, passwordCoder);
            if (b) {
                //原密码校验正确则将新密码加密后存储到数据库中
                //3.获取新密码并加密
                String newPassword= (String) map.get("newPassword");
                String newPasswordCode=encoder.encode(newPassword);
                userService.updatePassword(username,newPasswordCode);
                return new Result(true,"修改密码成功");

            }

           return new Result(false,"原密码输入错误，请重新输入");

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改密码错误!");
        }



    }

}
