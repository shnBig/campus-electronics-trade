package com.electronics.managerservice.controller;

import com.electronics.commonserver.entity.Permission;
import com.electronics.commonserver.result.Result;
import com.electronics.managerservice.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/manager/permission")
public class PermissionController {
    
    @Autowired
    private PermissionService permissionService;

    /**
     * 添加权限
     */
    @PostMapping("/add")
    public Result<Permission> addPermission(@RequestBody Permission permission) {
        log.info("添加权限: {}", permission.getPermissionName());
        Permission result = permissionService.addPermission(permission);
        return Result.success("添加成功", result);
    }

    /**
     * 更新权限
     */
    @PutMapping("/update")
    public Result<Permission> updatePermission(@RequestBody Permission permission) {
        log.info("更新权限: {}", permission.getId());
        Permission result = permissionService.updatePermission(permission);
        return Result.success("更新成功", result);
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deletePermission(@PathVariable("id") Integer id) {
        log.info("删除权限: {}", id);
        permissionService.deletePermission(id);
        return Result.success("删除成功", null);
    }

    /**
     * 根据ID查询权限
     */
    @GetMapping("/get/{id}")
    public Result<Permission> getPermission(@PathVariable("id") Integer id) {
        Permission permission = permissionService.getPermissionById(id);
        return Result.success(permission);
    }

    /**
     * 查询所有权限
     */
    @GetMapping("/list")
    public Result<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return Result.success(permissions);
    }
}
