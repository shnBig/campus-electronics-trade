package com.electronics.commonserver.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterVO {
    private String token;
    private Integer id;
    private String username;
    private String nickName;
    private String phone;
    private String email;
    private String avater;
    private Integer gender;
    private Integer creditScore;
    private Integer status;
}
