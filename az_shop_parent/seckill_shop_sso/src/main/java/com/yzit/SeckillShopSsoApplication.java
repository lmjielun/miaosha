package com.yzit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SeckillShopSsoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeckillShopSsoApplication.class, args);
	}

}
