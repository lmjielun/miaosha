package com.yzit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient //启动eurka客户端
@EnableFeignClients // 开启Feign功能
public class SpeckillPayApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpeckillPayApiApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		// 默认的RestTemplate，底层是走JDK的URLConnection方式。
		return new RestTemplate();
	}

}
