package com.electronics.managerservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 接口性能监控切面
 * 用于监控接口执行时间，排查性能问题
 */
@Slf4j
@Aspect
@Component
public class PerformanceMonitorAspect {

    /**
     * 定义切入点：监控 role 相关的所有接口
     */
    @Pointcut("execution(* com.electronics.managerservice.controller.RoleController.*(..))")
    public void roleControllerPointcut() {
    }

    /**
     * 定义切入点：监控 permission 相关的所有接口
     */
    @Pointcut("execution(* com.electronics.managerservice.controller.PermissionController.*(..))")
    public void permissionControllerPointcut() {
    }

    /**
     * 环绕通知：监控方法执行时间
     */
    @Around("roleControllerPointcut() || permissionControllerPointcut()")
    public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法信息
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        log.info("========== 开始执行: {}.{} ==========", className, methodName);
        
        try {
            // 执行目标方法
            Object result = joinPoint.proceed();
            
            // 记录结束时间
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            // 记录执行时间和结果
            log.info("========== 执行完成: {}.{} ==========", className, methodName);
            log.info("执行耗时: {} ms", executionTime);
            
            // 如果执行时间超过 1 秒，给出警告
            if (executionTime > 1000) {
                log.warn("⚠️ 接口执行时间过长: {}.{} 耗时 {} ms", className, methodName, executionTime);
            }
            
            return result;
        } catch (Throwable e) {
            // 记录异常和耗时
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            log.error("❌ 执行异常: {}.{} 耗时 {} ms, 异常信息: {}", 
                    className, methodName, executionTime, e.getMessage(), e);
            throw e;
        }
    }
}
