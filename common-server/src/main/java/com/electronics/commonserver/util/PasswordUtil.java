package com.electronics.commonserver.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class PasswordUtil {

    /**
     * 生成随机盐值
     */
    public static String generateSalt() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 密码 + 盐值 加密
     */
    public static String encryptPassword(String password, String salt) {
        try {
            String saltedPassword = password + salt;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(saltedPassword.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("密码加密失败", e);
        }
    }

    /**
     * 验证密码
     */
    public static boolean verifyPassword(String inputPassword, String salt, String storedPassword) {
        String encryptedPassword = encryptPassword(inputPassword, salt);
        return encryptedPassword.equals(storedPassword);
    }
}
