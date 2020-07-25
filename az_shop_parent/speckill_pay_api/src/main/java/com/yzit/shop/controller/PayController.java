package com.yzit.shop.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.yzit.config.AlipayConfig;
import com.yzit.framework.web.ui.AjaxResult;
import com.yzit.shop.service.PayFeginClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/alipay")
@CrossOrigin
@Api("支付接口")
public class PayController {

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PayFeginClient payFeginClient;

    /*@Autowired
    private PayFeginClient payFeginClient;*/

    /**
     *  同步调用接口
     * @param orderNo
     * @param subject
     * @param amount
     * @param body
     * @return
     */
    @GetMapping(value="/pay",produces = "application/json;charset=UTF-8")
    @ApiOperation(value="测试支付2")
    public AjaxResult pay(@ApiParam( name="订单编号" ) @RequestParam String orderNo, @ApiParam( name="订单名称" ) @RequestParam String subject,
                          @ApiParam(name="订单金额" ) @RequestParam double amount, @ApiParam(name="订单描述" ) @RequestParam String body  ){
        try{//设置请求参数
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());
            alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());
            alipayRequest.setBizContent("{\"out_trade_no\":\""+ orderNo +"\","
                    + "\"total_amount\":\""+ amount +"\","
                    + "\"subject\":\""+ subject +"\","
                    + "\"body\":\""+ body +"\","
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
            //发送请求请求
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            AjaxResult  ajax = new AjaxResult();
            ajax.setSuccess(true);
            ajax.setCode(200);
            ajax.setObj(result);//设置响应结果
            return ajax;
        }catch(Exception e){
            e.printStackTrace();
            return  AjaxResult.ERROR(e.getMessage());
        }
    }


    /***
     * 异步回调地址
     * @param paramMap
     * @return
     * @throws AlipayApiException
     */
    @RequestMapping("/notify")
    public String notify(@RequestParam Map<String,String> paramMap) throws AlipayApiException {
        //验证签名
        boolean verifyResult = AlipaySignature.rsaCheckV1(paramMap, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset(), alipayConfig.getSignType());

        System.out.println("paramMap = "+paramMap);

        if (verifyResult) {
            String tradeStatus = paramMap .get("trade_status"); // 交易状态
            String outTradeNo = paramMap .get ("out_trade_no"); // 商户订单号
            System.out.println("支付宝获取的订单号==" + outTradeNo);
            String tradeNo = paramMap .get("trade_no"); // 支付宝订单号

            System.out.println("交易状态 :"+tradeStatus);
            System.out.println("商户订单号 :"+outTradeNo);
            System.out.println("支付宝订单号 :"+tradeNo);

            // 远程调用秒杀接口：修改订单状态接口
            payFeginClient.updateOrderStatus(outTradeNo,tradeNo);

            // 远程调用秒杀接口：修改订单状态接口
           /* Map<String, Object> params = new HashMap<String, Object>();
            params.put("orderNo", outTradeNo); // 订单编号
            params.put("transactionId", tradeNo); // 支付宝流水号
            String url = "http://localhost:8081/seckillOrder/updateOrderStatus?orderNo={orderNo}&transactionId={transactionId}";//调用修改订单接口
            AjaxResult json = restTemplate.getForObject(url, AjaxResult.class, params);*/

            return "success";
        }
        //   return "fail";
        return "error: not exists out_trade_no:";

    }

}


