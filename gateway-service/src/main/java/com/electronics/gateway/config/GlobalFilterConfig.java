package com.electronics.gateway.config;

import com.electronics.commonserver.util.JwtUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class GlobalFilterConfig implements GlobalFilter, Ordered {
    private static final String AUTH_TOKEN = "authorization";
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("=========拦截器========");

        // 获取请求对象
        ServerHttpRequest request = exchange.getRequest();

        // 获取响应对象
        ServerHttpResponse response = exchange.getResponse();

        //获取请求地址
        String url = request.getURI().getPath();
        //获取token信息
        String token = request.getHeaders().getFirst(AUTH_TOKEN);

        // 如果不需要进行验证的接口我们就直接放行
        if(this.shouldNotFilter(url)){
            return chain.filter(exchange);
        }

        // 验证token是否为 空
        if(StringUtil.isNullOrEmpty( token)){
            return unAuthorize(exchange);
        }

        // 验证redis中是否存在token（使用 token: 前缀）
        String tokenKey = "token:" + token;
        if(!redisTemplate.hasKey(tokenKey)){
            return unAuthorize(exchange);
        }

        // 把新的exchange放回到过滤器中
        ServerHttpRequest newRequest = request.mutate().header(AUTH_TOKEN, token).build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        return chain.filter(newExchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }


    private boolean shouldNotFilter(String url){
        if(url.startsWith("/user/login")){
            return true;
        }
        if(url.startsWith("/user/register")){
            return true;
        }
        if(url.startsWith("/manager/login")){
            return true;
        }
        if(url.startsWith("/manager/register")){
            return true;
        }
        return false;
    }
    private Mono<Void> unAuthorize(ServerWebExchange exchange){
        // 设置错误状态码
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        // 设置返回的信息为JSON类型
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        // 自定义错误信息
        String errorMsg = "{\"code\":\"401\",\"msg\":\"未授权\"}";
        // 将自定义错误响应写入响应体
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(errorMsg.getBytes())));
    }
}
