package com.mosh.srb.sms.controller;

import com.mosh.common.exception.Assert;
import com.mosh.common.result.R;
import com.mosh.common.result.ResponseEnum;
import com.mosh.srb.base.utils.RandomUtils;
import com.mosh.srb.base.utils.RegexValidateUtils;
import com.mosh.srb.sms.client.CoreUserInfoClient;
import com.mosh.srb.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/8/31
 */
@Api(tags = "SMS短信管理")
@RequestMapping("/api/sms")
@CrossOrigin
@RestController
public class ApiSmsController {

    @Resource
    private SmsService smsService;

    @Resource
    private CoreUserInfoClient coreUserInfoClient;

    @ApiOperation("test")
    @GetMapping("/test")
    public R test(){
        boolean result = coreUserInfoClient.checkMobile("13517029677");

        return R.ok().data("result",result);
    }

    @ApiOperation("获取验证码")
    @GetMapping("/send/{mobile}")
    public R send(@ApiParam("手机号码")
                  // @PathVariable不加参数时，不传参数会抛404异常，加入required = false后，传空参数会拼接 undefined
                  @PathVariable(value = "mobile",required = false) String mobile){

        // 对手机号校验
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);

        // 生成随机四位数字验证码
        String fourBitRandom = RandomUtils.getFourBitRandom();
        Map<String,Object> map = new HashMap<>();
        map.put("code",fourBitRandom);

        //发送短信
        smsService.send(mobile,map);

        return R.ok().message("短信发送成功");
    }

}
