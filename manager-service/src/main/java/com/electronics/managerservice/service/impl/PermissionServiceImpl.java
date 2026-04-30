package com.electronics.managerservice.service.impl;

import com.electronics.commonserver.entity.Permission;
import com.electronics.commonserver.exception.BusinessException;
import com.electronics.managerservice.mapper.PermissionMapper;
import com.electronics.managerservice.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    @Transactional
    public Permission addPermission(Permission permission) {
        permission.setCreateTime(LocalDateTime.now());
        permissionMapper.insert(permission);
        return permission;
    }

    @Override
    @Transactional
    public Permission updatePermission(Permission permission) {
        Permission existPermission = permissionMapper.selectById(permission.getId());
        if (existPermission == null) {
            throw new BusinessException(404, "权限不存在");
        }
        permissionMapper.updateById(permission);
        return permission;
    }

    @Override
    @Transactional
    public void deletePermission(Integer id) {
        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException(404, "权限不存在");
        }
        permissionMapper.deleteById(id);
    }

    @Override
    public Permission getPermissionById(Integer id) {
        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException(404, "权限不存在");
        }
        return permission;
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionMapper.selectList(null);
    }
}
