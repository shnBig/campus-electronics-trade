package com.electronics.orderservice.controller;

import com.electronics.commonserver.entity.Order;
import com.electronics.orderservice.feignClient.PointServiceFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RefreshScope //实现nacos配置热加载
public class OrderController {
    //获取到nacos中的配置
    @Value("${config.info}")
    private String nacosConfig;

    @Autowired
    private PointServiceFeignClient pointServiceFeignClient;

    @GetMapping("/test")
    public String test() {
        System.out.println("Order Service");
        return "这是一个测试";
    }

    @GetMapping("/nacos/getConfig")
    public String getConfig() {
        return nacosConfig;
    }

    @PostMapping("/add")
    public String addOrder() {
        Order order = new Order("1","手机");
        return pointServiceFeignClient.addPoint(order);
    }
}
