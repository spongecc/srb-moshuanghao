package com.mosh.srb.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/8/25
 */
@SpringBootApplication
@ComponentScan({"com.mosh.srb","com.mosh.common"})
@EnableDiscoveryClient
public class ServiceCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCoreApplication.class,args);
    }
}
