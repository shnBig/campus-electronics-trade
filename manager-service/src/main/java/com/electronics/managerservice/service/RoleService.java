package com.electronics.managerservice.service;

import com.electronics.commonserver.entity.Role;

import java.util.List;

public interface RoleService {
    /**
     * 添加角色
     */
    Role addRole(Role role);

    /**
     * 更新角色
     */
    Role updateRole(Role role);

    /**
     * 删除角色
     */
    void deleteRole(Integer id);

    /**
     * 根据ID查询角色
     */
    Role getRoleById(Integer id);

    /**
     * 查询所有角色
     */
    List<Role> getAllRoles();

    /**
     * 为角色分配权限
     */
    void assignPermissions(Integer roleId, List<Integer> permissionIds);

    /**
     * 查询角色的权限列表
     */
    List<Integer> getRolePermissions(Integer roleId);
}
