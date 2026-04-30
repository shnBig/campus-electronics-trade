package com.electronics.commonserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 权限实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("permission")
public class Permission {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String permissionName;

    private String permissionCode;

    private String path;

    private LocalDateTime createTime;
}
