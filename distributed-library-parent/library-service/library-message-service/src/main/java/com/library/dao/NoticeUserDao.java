package com.library.dao;

import com.library.pojo.NoticeUser;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface NoticeUserDao extends Mapper<NoticeUser> {
   //查询该用户已经读了的公告id
    @Select(" select notice.id from notice left join notice_user on notice.id=notice_user.nid where  notice_user.uid=#{uid}")
    public List<Integer> selectHasRead(Integer uid);
}
