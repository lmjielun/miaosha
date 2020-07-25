package com.yzit.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration   // 该注解表示当前类是一个配置容器，当前类就是xml配置文件
@MapperScan({ "com.yzit.shop.dao" })
public class MyBatisConfig {
}
