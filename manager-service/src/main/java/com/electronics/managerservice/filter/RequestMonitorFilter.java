package com.electronics.managerservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求链路监控过滤器
 * 监控整个请求的处理时间，包括网关、过滤器等所有环节
 */
@Slf4j
@Component
public class RequestMonitorFilter extends OncePerRequestFilter {

    private static final String START_TIME_ATTR = "requestStartTime";

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        // 记录请求开始时间
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME_ATTR, startTime);
        
        String uri = request.getRequestURI();
        String method = request.getMethod();
        
        log.info("🔵 请求开始: {} {} | 开始时间: {}", method, uri, startTime);
        
        try {
            // 继续处理请求
            filterChain.doFilter(request, response);
            
            // 记录请求结束时间
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            
            log.info(" 请求完成: {} {} | 总耗时: {} ms | 结束时间: {}", 
                    method, uri, totalTime, endTime);
            
            // 如果总耗时超过 1 秒，给出警告
            if (totalTime > 1000) {
                log.warn("⚠️ 请求总耗时过长: {} {} 总耗时 {} ms", method, uri, totalTime);
            }
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            log.error("🔴 请求异常: {} {} | 总耗时: {} ms | 异常: {}", 
                    method, uri, totalTime, e.getMessage(), e);
            throw e;
        }
    }
}
