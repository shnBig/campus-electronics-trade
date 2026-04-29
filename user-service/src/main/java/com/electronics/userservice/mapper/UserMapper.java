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
    User selectByUserName(@Param("userName") String userName);

    /**
     * 根据用户ID查询用户角色列表
     */
    List<String> selectRolesByUserId(@Param("userId") Integer userId);
}
