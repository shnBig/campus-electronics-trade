package com.electronics.commonserver.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil  {
    private static final String USER_NAME = "admin";

    //用于签名加密的密钥，为一个字符串
    private static final String KEY = "token_key";

    public static final int TOKEN_TIME_OUT = 1000*60*100;

    public static String getToken(String userId){
        //生成jwt token
        JWTCreator.Builder builder = JWT.create();

        //设置Header
        Map<String,Object> headers = new HashMap<>();

        headers.put("typ","JWT");
        headers.put("alg","HS256");

        //开始生成token
        String token = builder.withHeader(headers)
                .withClaim("userId",userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_TIME_OUT))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                // token的发行者
                .withIssuer(USER_NAME)
                //进行签名 配合加密
                .sign(Algorithm.HMAC256( KEY));

        System.out.println("token:"+token);
        return token;
    }

    public static boolean verify(String token){
        try {
            JWT.decode(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
