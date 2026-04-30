package com.electronics.managerservice.service;

import com.electronics.commonserver.entity.Permission;

import java.util.List;

public interface PermissionService {
    /**
     * 添加权限
     */
    Permission addPermission(Permission permission);

    /**
     * 更新权限
     */
    Permission updatePermission(Permission permission);

    /**
     * 删除权限
     */
    void deletePermission(Integer id);

    /**
     * 根据ID查询权限
     */
    Permission getPermissionById(Integer id);

    /**
     * 查询所有权限
     */
    List<Permission> getAllPermissions();
}
