package com.electronics.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.electronics.commonserver.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     */
    User selectByUserName(@Param("username") String username);


}
