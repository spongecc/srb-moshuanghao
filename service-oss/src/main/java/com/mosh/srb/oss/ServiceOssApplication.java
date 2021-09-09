package com.mosh.srb.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/9/7
 */
@ComponentScan({"com.mosh.srb","com.mosh.common"})
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceOssApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceOssApplication.class,args);
    }

}
