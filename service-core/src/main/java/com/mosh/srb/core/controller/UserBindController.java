package com.mosh.srb.core.controller;


import com.mosh.common.exception.Assert;
import com.mosh.common.result.R;
import com.mosh.common.result.ResponseEnum;
import com.mosh.srb.base.utils.JwtUtils;
import com.mosh.srb.core.hfb.RequestHelper;
import com.mosh.srb.core.pojo.vo.UserBindVO;
import com.mosh.srb.core.service.UserBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 前端控制器
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
@Api(tags = "用户信息绑定接口")
@RestController
@RequestMapping("/api/core/userBind")
public class UserBindController {

    @Resource
    private UserBindService userBindService;

    @ApiOperation("账户绑定异步回调")
    @PostMapping("notify")
    public String notify(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> map = RequestHelper.switchMap(parameterMap);

        boolean b = RequestHelper.isSignEquals(map);
        Assert.isTrue(b, ResponseEnum.HFB_SIGN_ERROR);

        userBindService.bindNotify(map);

        return "success";
    }


    @ApiOperation("账户绑定提交数据")
    @PostMapping("auth/bind")
    public R bind(@RequestBody UserBindVO userBindVO, HttpServletRequest request){

        // 从请求中获取 token 中的 userId
        String token = request.getHeader("token");

        // 未获取到 token 抛出未登录
        Assert.notNull(token,ResponseEnum.LOGIN_AUTH_ERROR);
        Long userId = JwtUtils.getUserId(token);

        String formStr = userBindService.commitBindUser(userBindVO,userId);

        return R.ok().data("formStr",formStr);
    }

}

