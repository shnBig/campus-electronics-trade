package com.electronics.orderservice.feignClient;

import com.electronics.commonserver.entity.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value="point-service")
public interface PointServiceFeignClient {
    @PostMapping("/point/addPoint")
    String addPoint(@RequestBody Order order);
}
