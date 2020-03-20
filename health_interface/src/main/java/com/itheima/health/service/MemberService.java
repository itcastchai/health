package com.itheima.health.service;

import com.itheima.health.pojo.Member;

public interface MemberService {
    void addMember(Member member1);

    Member findByTelephone(String telephone);
}
