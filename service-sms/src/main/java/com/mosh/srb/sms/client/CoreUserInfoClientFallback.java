package com.mosh.srb.sms.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/9/6
 */
@Component
@Slf4j
public class CoreUserInfoClientFallback implements CoreUserInfoClient{
    @Override
    public boolean checkMobile(String mobile) {
        log.error("远程调用失败，熔断降级处理");
        System.out.println("调用服务出现异常，返回兜底值");
        return false;
    }
}
