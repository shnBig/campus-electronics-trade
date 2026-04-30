package com.electronics.managerservice.GlobalHandler;

import com.electronics.commonserver.exception.BusinessException;
import com.electronics.commonserver.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一处理所有Controller层抛出的异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     * @param e 业务异常
     * @return 统一响应结果
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.error("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理数据库唯一约束冲突异常
     * @param e 重复键异常
     * @return 统一响应结果
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException e) {
        log.warn("数据重复约束冲突: {}", e.getMessage());
        String message = "数据已存在";
        String errorMessage = e.getMessage();
        if (errorMessage != null) {
            if (errorMessage.contains("uk_role_name")) {
                message = "角色名称已存在";
            } else if (errorMessage.contains("uk_username") || errorMessage.contains("username")) {
                message = "用户名已存在";
            } else if (errorMessage.contains("uk_email") || errorMessage.contains("email")) {
                message = "邮箱已存在";
            }
        }

        return Result.error(400, message);
    }

    /**
     * 处理其他所有未捕获的异常
     * @param e 异常
     * @return 统一响应结果
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return Result.error(500, "系统错误，请联系管理员");
    }
}
