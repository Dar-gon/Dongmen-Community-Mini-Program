package com.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 东门社区便民云服务平台启动类
 */
@SpringBootApplication
@MapperScan("com.community.mapper")
@EnableScheduling
//@EnableDiscoveryClient
public class CommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
        System.out.println("====================================");
        System.out.println("东门社区便民云服务平台启动成功！");
        System.out.println("API文档地址: http://localhost:8080/doc.html");
        System.out.println("====================================");
    }
}
