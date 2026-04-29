package com.electronics.managerservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.electronics.commonserver.entity.Manager;
import com.electronics.commonserver.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ManagerMapper extends BaseMapper<Manager> {

    /**
     * 根据用户名查询用户
     */
    Manager selectByUserName(@Param("username") String username);

    /**
     * 根据用户ID查询用户角色列表
     */
    List<String> selectRolesByUserId(@Param("userId") Integer userId);
}