package com.mosh.srb.core.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mosh.common.result.R;
import com.mosh.srb.core.pojo.entity.UserInfo;
import com.mosh.srb.core.pojo.query.UserInfoQuery;
import com.mosh.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
//@CrossOrigin //通过gateway网关自动添加跨域配置
@Api(tags = "会员后台接口")
@RestController
@RequestMapping("/admin/core/userInfo")
@Slf4j
public class AdminUserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("获取会员分页列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam("页码") @PathVariable("page") Long page,
                        @ApiParam("每页条数") @PathVariable("limit") Long limit,
                        @ApiParam("查询条件对象") UserInfoQuery userInfoQuery){
        Page<UserInfo> userInfoPage = new Page<>(page,limit);
        IPage<UserInfo> pageModel = userInfoService.listPage(userInfoPage,userInfoQuery);
        return R.ok().data("pageModel",pageModel);
    }

    @ApiOperation("锁定和解锁")
    @PutMapping("lock/{id}/{status}")
    public R lock(@ApiParam("用户id") @PathVariable("id") Long id,
                  @ApiParam("锁定状态（0：锁定 1：解锁）") @PathVariable("status") Integer status){
        userInfoService.lock(id,status);
        return R.ok().message(status==1?"解锁成功":"锁定成功");
    }

}

