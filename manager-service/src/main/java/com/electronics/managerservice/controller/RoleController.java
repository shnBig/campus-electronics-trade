package com.electronics.managerservice.controller;

import com.electronics.commonserver.dto.RoleAssignPermissionDTO;
import com.electronics.commonserver.entity.Role;
import com.electronics.commonserver.result.Result;
import com.electronics.managerservice.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/manager/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 添加角色
     */
    @PostMapping("/add")
    public Result addRole(@RequestBody Role role) {
        log.info("添加角色: {}", role.getRoleName());
        roleService.addRole(role);
        return Result.success("添加角色成功", null);
    }

    /**
     * 更新角色
     */
    @PutMapping("/update")
    public Result<Role> updateRole(@RequestBody Role role) {
        log.info("更新角色: {}", role.getId());
        Role result = roleService.updateRole(role);
        return Result.success("更新成功", result);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteRole(@PathVariable("id") Integer id) {
        log.info("删除角色: {}", id);
        roleService.deleteRole(id);
        return Result.success("删除成功", null);
    }

    /**
     * 根据ID查询角色
     */
    @GetMapping("/get/{id}")
    public Result<Role> getRole(@PathVariable("id") Integer id) {
        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

    /**
     * 查询所有角色
     */
    @GetMapping("/list")
    public Result<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return Result.success(roles);
    }

    /**
     * 为角色分配权限
     */
    @PostMapping("/assignPermissions")
    public Result<Void> assignPermissions(@RequestBody RoleAssignPermissionDTO params) {
        log.info("为角色分配权限: roleId={}, permissionIds={}",
                params.getRoleId(), params.getPermissionIds());
        roleService.assignPermissions(params.getRoleId(), params.getPermissionIds());
        return Result.success("分配成功", null);
    }

    /**
     * 查询角色的权限列表
     */
    @GetMapping("/permissions/{roleId}")
    public Result<List<Integer>> getRolePermissions(@PathVariable("roleId") Integer roleId) {
        List<Integer> permissionIds = roleService.getRolePermissions(roleId);
        return Result.success(permissionIds);
    }
}
