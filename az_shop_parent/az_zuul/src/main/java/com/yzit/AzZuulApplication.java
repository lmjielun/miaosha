package com.yzit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableDiscoveryClient   //启动eureka客户端
@EnableZuulProxy //启动zuul服务网关
public class AzZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(AzZuulApplication.class, args);
	}

}
