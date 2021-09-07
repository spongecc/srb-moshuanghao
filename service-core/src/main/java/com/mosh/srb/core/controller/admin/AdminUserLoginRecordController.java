package com.mosh.srb.core.controller.admin;


import com.mosh.common.result.R;
import com.mosh.srb.core.pojo.entity.UserLoginRecord;
import com.mosh.srb.core.service.UserLoginRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户登录记录表 前端控制器
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
//@CrossOrigin //通过gateway网关自动添加跨域配置
@Api(tags = "会员登录日志接口")
@RestController
@RequestMapping("/admin/core/userLoginRecord")
@Slf4j
public class AdminUserLoginRecordController {

    @Resource
    private UserLoginRecordService userLoginRecordService;

    @ApiOperation("获取会员登录日志列表")
    @GetMapping("/listTop50/{userId}")
    public R listTop50(@ApiParam("会员ID") @PathVariable("userId") Long userId){
        List<UserLoginRecord> loginRecordList = userLoginRecordService.listTop50(userId);
        return R.ok().data("list",loginRecordList);
    }

}

