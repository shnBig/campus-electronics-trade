package com.electronics.managerservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.electronics.commonserver.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限Mapper接口
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}
