package com.electronics.commonserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Integer userId;
    private String username;
    private String nickName;
    private String avatar;
    private List<String> roles; // 用户角色列表

}
