package com.electronics.managerservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.electronics.commonserver.entity.Permission;
import com.electronics.commonserver.entity.Role;
import com.electronics.commonserver.entity.RolePermission;
import com.electronics.commonserver.exception.BusinessException;
import com.electronics.managerservice.mapper.PermissionMapper;
import com.electronics.managerservice.mapper.RoleMapper;
import com.electronics.managerservice.mapper.RolePermissionMapper;
import com.electronics.managerservice.service.PermissionService;
import com.electronics.managerservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    @Transactional
    public Role addRole(Role role) {
        role.setCreateTime(LocalDateTime.now());
        roleMapper.insert(role);
        return role;
    }

    @Override
    @Transactional
    public Role updateRole(Role role) {
        Role existRole = roleMapper.selectById(role.getId());
        if (existRole == null) {
            throw new BusinessException(404, "角色不存在");
        }
        roleMapper.updateById(role);
        return role;
    }

    @Override
    @Transactional
    public void deleteRole(Integer id) {
//        Role role = roleMapper.selectById(id);
//        if (role == null) {
//            throw new BusinessException(404, "角色不存在");
//        }
        // 删除角色权限关联
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, id);
        rolePermissionMapper.delete(wrapper);
        
        roleMapper.deleteById(id);
    }

    @Override
    public Role getRoleById(Integer id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(404, "角色不存在");
        }
        return role;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleMapper.selectList(null);
    }

    @Override
    @Transactional
    public void assignPermissions(Integer roleId, List<Integer> permissionIds) {
        // 检查角色是否存在
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(404, "角色不存在");
        }

        // 删除旧的角色权限关联
        LambdaQueryWrapper<RolePermission> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(RolePermission::getRoleId, roleId);
        rolePermissionMapper.delete(deleteWrapper);

        // 添加新的角色权限关联
        for (Integer permissionId : permissionIds) {
            // 检查权限是否存在
            Permission permission = permissionMapper.selectById(permissionId);
            if (permission == null) {
                throw new BusinessException(404, "权限不存在: " + permissionId);
            }

            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionMapper.insert(rolePermission);
        }
    }

    @Override
    public List<Integer> getRolePermissions(Integer roleId) {
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(wrapper);
        return rolePermissions.stream()
                .map(RolePermission::getPermissionId)
                .toList();
    }
}
