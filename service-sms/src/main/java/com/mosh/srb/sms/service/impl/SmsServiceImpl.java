package com.mosh.srb.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.mosh.common.exception.Assert;
import com.mosh.common.exception.BusinessException;
import com.mosh.common.result.ResponseEnum;
import com.mosh.srb.sms.service.SmsService;
import com.mosh.srb.sms.util.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/8/31
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void send(String mobile, Map<String, Object> param) {
        try {
            DefaultProfile defaultProfile = DefaultProfile.getProfile(
                SmsProperties.REGION_Id,
                SmsProperties.KEY_ID,
                SmsProperties.KEY_SECRET
            );
            IAcsClient iAcsClient = new DefaultAcsClient(defaultProfile);

            CommonRequest commonRequest = new CommonRequest();
            //创建远程连接的请求参数
            commonRequest.setSysMethod(MethodType.POST);
            commonRequest.setSysDomain("dysmsapi.aliyuncs.com");
            commonRequest.setSysVersion("2017-05-25");
            commonRequest.setSysAction("SendSms");
            commonRequest.putQueryParameter("RegionId", SmsProperties.REGION_Id);
            commonRequest.putQueryParameter("PhoneNumbers", mobile);
            commonRequest.putQueryParameter("SignName", SmsProperties.SIGN_NAME);
            commonRequest.putQueryParameter("TemplateCode", SmsProperties.TEMPLATE_CODE);

            Gson gson = new Gson();
            //Map<String,String> map = new HashMap<>();
            //map.put("code","123456");
            String json = gson.toJson(param);
            commonRequest.putQueryParameter("TemplateParam", json);

            CommonResponse commonResponse = iAcsClient.getCommonResponse(commonRequest);

            // 确认短信服务是否有响应
            boolean success = commonResponse.getHttpResponse().isSuccess();
            Assert.isTrue(success,ResponseEnum.ALIYUN_RESPONSE_FAIL);

            // 获取响应的数据，转换成json，获取Code
            String responseData = commonResponse.getData();
            Map<String,String> map = gson.fromJson(responseData, HashMap.class);
            String code = map.get("Code");

            //ALIYUN_SMS_LIMIT_CONTROL_ERROR(-502, "短信发送过于频繁"),//业务限流
            Assert.notEquals("isv.BUSINESS_LIMIT_CONTROL", code, ResponseEnum.ALIYUN_SMS_LIMIT_CONTROL_ERROR);
            //ALIYUN_SMS_ERROR(-503, "短信发送失败"),//其他失败
            Assert.equals("OK", code, ResponseEnum.ALIYUN_SMS_ERROR);

            // 将手机对应的验证码存入redis
            //redisTemplate.opsForValue().set("srb:sms:code:" + mobile, param.get("code"), 5, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set("srb:sms:code:" + mobile, param.get("code"));
        } catch (ClientException e) {
            log.error("阿里云短信发送SDK调用失败：");
            log.error("ErrorCode=" + e.getErrCode());
            log.error("ErrorMessage=" + e.getErrMsg());
            throw new BusinessException(ResponseEnum.ALIYUN_SMS_ERROR , e);
        }
    }
}
