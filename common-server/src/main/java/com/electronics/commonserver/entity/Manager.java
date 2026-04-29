package com.electronics.commonserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class Manager {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("user_name")
    private String username;

    private String nickName;

    private String phone;

    private String email;

    private String password;

    private String avatar;

    private String status;

    private LocalDateTime createTime;

    private String salt;
}
