package com.electronics.commonserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

    private String username;
    private String nickName;
    private String password;
    private String email;
    private String phone;
}
