package com.yzit.shop.service;

import com.yzit.framework.web.ui.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *  客户端调用 服务端 提供的接口
 */
@FeignClient(value = "item-service") // 指向Eureka里的【服务者】注册的应用名称
public interface PayFeginClient {

    /**
     *  客户端 调用 服务端接口
     *  Get 接收多个参数 需要指定参数名称  使用@RequestParm
     * @param orderNo
     * @param transactionId
     * @return
     */
    @GetMapping("seckillOrder/updateOrderStatus")
    public AjaxResult updateOrderStatus(@RequestParam("orderNo") String orderNo, @RequestParam("transactionId") String transactionId);


}
