package com.itheima.health.dao;

import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;

public interface MemberDao {

    Member findByMemberTelephone(String telephone);

    void saveMember(Member member1);

    void addMember(Member member);

    Member findByTelephone(String telephone);


    Integer countMemberfindBySetmealId(Integer id);

    Integer findTodayNewMember(String reportDate);

    Integer findTotalMember();

    Integer findThisWeekNewMember(String firstWeekMonday);

    Integer findThisMonthNewMember(String firstMonthDay);
}
