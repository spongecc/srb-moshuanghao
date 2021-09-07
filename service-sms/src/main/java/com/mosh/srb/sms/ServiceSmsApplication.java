package com.mosh.srb.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/8/31
 */
@SpringBootApplication
@ComponentScan({"com.mosh.srb", "com.mosh.common"})
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSmsApplication.class,args);
    }

}
