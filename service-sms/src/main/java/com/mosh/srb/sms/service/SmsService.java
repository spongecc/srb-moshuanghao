package com.mosh.srb.sms.service;

import java.util.Map;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/8/31
 */
public interface SmsService {

    void send(String mobile, Map<String,Object> param);

}
