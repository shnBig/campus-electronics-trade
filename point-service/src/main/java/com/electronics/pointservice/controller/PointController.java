package com.electronics.pointservice.controller;

import com.electronics.commonserver.entity.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/point")
public class PointController {
    //获取到nacos中的配置
    @Value("${config.info}")
    private String nacosConfig;
    @GetMapping("/test")
    public String test(){
        return "这是积分服务的测试接口";
    }

    @GetMapping("/nacos/config")
    public String getNacosConfig(){
        return nacosConfig;
    }

    @PostMapping("/addPoint")
    public String addPoint(@RequestBody Order order){
        return "添加积分成功! 商品名称为:"+order.getProductionName();
    }
}
