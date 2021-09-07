package com.mosh.srb.core.controller;


import com.mosh.common.exception.Assert;
import com.mosh.common.result.R;
import com.mosh.common.result.ResponseEnum;
import com.mosh.srb.base.utils.JwtUtils;
import com.mosh.srb.base.utils.RegexValidateUtils;
import com.mosh.srb.core.pojo.vo.LoginVO;
import com.mosh.srb.core.pojo.vo.RegisterVO;
import com.mosh.srb.core.pojo.vo.UserInfoVO;
import com.mosh.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
//@CrossOrigin //通过gateway网关自动添加跨域配置
@Api(tags = "会员接口")
@RestController
@RequestMapping("/api/core/userInfo")
@Slf4j
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("校验手机号是否注册")
    @GetMapping("/checkMobile/{mobile}")
    public boolean checkMobile(@PathVariable("mobile") String mobile){
        boolean b = userInfoService.checkMobile(mobile);
        //返回 false 表示已注册
        return b;
    }

    @ApiOperation("会员登录")
    @GetMapping("checkToken/{token}")
    public R checkToken(@PathVariable("token") String token){
        boolean checkToken = JwtUtils.checkToken(token);
        // checkToken为false 用户未登录
        Assert.isTrue(checkToken,ResponseEnum.LOGIN_AUTH_ERROR);
        return R.ok();
    }


    @ApiOperation("会员登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVO loginVO, HttpServletRequest request){

        // 判断账号密码是否为空
        Assert.notEmpty(loginVO.getMobile(),ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(loginVO.getPassword(),ResponseEnum.PASSWORD_NULL_ERROR);

        // 获取用户IP
        String ip = request.getRemoteAddr();
        request.getHeader("X-forwarded-for");
        UserInfoVO userInfoVO = userInfoService.login(loginVO,ip);
        return R.ok().message("登录成功").data("userInfo",userInfoVO);
    }

    @ApiOperation("注册会员")
    @PostMapping("register")
    public R register(@RequestBody RegisterVO registerVO){
        String mobile = registerVO.getMobile();
        String password = registerVO.getPassword();
        String code = registerVO.getCode();

        // 判断手机号不能为空，手机号是否正确，密码和验证码是否为空
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notEmpty(code,ResponseEnum.CODE_NULL_ERROR);

        userInfoService.regist(registerVO);

        return R.ok();
    }

}

