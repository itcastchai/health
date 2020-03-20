package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import com.itheima.health.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService{
   @Autowired
   private MemberDao memberDao;


    @Override
    public Member findByTelephone(String telephone) {


        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void addMember(Member member) {
        if (member.getPassword()!=null){
            //密码加密
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.addMember(member);


    }
}
