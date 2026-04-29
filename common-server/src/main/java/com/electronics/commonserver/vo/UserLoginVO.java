package com.electronics.commonserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO {
    private String token;
    private Integer userId;
    private String username;
    private String nickName;
    private Integer gender;
    private Integer status;
    private String avatar;
}
