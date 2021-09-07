package com.mosh.srb.sms.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/8/25
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket adminApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("smsApi")
                .apiInfo(new ApiInfoBuilder()
                        .title("尚融宝短信系统-API文档")
                        .description("本文档描述了尚融宝sms短信接口")
                        .version("1.0")
                        .contact(new Contact("莫双豪", "http://admin.com", "1446605076@qq.com"))
                        .build()
                )
                .select()
                .paths(Predicates.and(PathSelectors.regex("/.*")))
                .build();
    }
}
