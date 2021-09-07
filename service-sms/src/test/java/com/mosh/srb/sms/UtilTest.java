package com.mosh.srb.sms;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.mosh.srb.sms.util.SmsProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/8/31
 */
@SpringBootTest
public class UtilTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void sendSmsTest(){
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
            commonRequest.putQueryParameter("PhoneNumbers", "13517029677");
            commonRequest.putQueryParameter("SignName", SmsProperties.SIGN_NAME);
            commonRequest.putQueryParameter("TemplateCode", SmsProperties.TEMPLATE_CODE);

            Gson gson = new Gson();
            Map<String,String> map = new HashMap<>();
            map.put("code","123456");
            String json = gson.toJson(map);
            commonRequest.putQueryParameter("TemplateParam", json);

            CommonResponse commonResponse = iAcsClient.getCommonResponse(commonRequest);

            System.out.println("commonResponse = " + commonResponse);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProperties(){
        System.out.println(SmsProperties.KEY_ID);
        System.out.println(SmsProperties.KEY_SECRET);
        System.out.println(SmsProperties.SIGN_NAME);
    }

}
