package com.mosh.srb.core.controller;


import com.mosh.common.exception.Assert;
import com.mosh.common.result.R;
import com.mosh.common.result.ResponseEnum;
import com.mosh.srb.base.utils.JwtUtils;
import com.mosh.srb.core.pojo.vo.BorrowerVO;
import com.mosh.srb.core.service.BorrowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
@Api(tags = "借款人")
@RestController
@RequestMapping("/api/core/borrower")
@Slf4j
public class BorrowerController {

    @Resource
    private BorrowerService borrowerService;

    @ApiOperation("保存借款人信息")
    @PostMapping("save")
    public R save(@ApiParam("借款人对象") @RequestBody BorrowerVO borrowerVO, HttpServletRequest request){

        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Assert.notNull(token, ResponseEnum.LOGIN_AUTH_ERROR);

        borrowerService.saveBorrowerVOByUserId(borrowerVO,userId);

        return R.ok().message("借款人信息保存成功");
    }
}

