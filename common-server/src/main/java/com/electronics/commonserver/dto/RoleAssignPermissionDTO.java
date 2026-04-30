package com.electronics.commonserver.dto;

import lombok.Data;

import java.util.List;
@Data
public class RoleAssignPermissionDTO {
    private Integer roleId;
    private List<Integer> permissionIds;
}
