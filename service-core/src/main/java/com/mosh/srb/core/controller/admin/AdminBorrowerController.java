package com.mosh.srb.core.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mosh.common.result.R;
import com.mosh.srb.core.pojo.entity.Borrower;
import com.mosh.srb.core.pojo.vo.BorrowerDetailVO;
import com.mosh.srb.core.service.BorrowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
@Api(tags = "借款人后台管理")
@RestController
@RequestMapping("/admin/core/borrower")
@Slf4j
public class AdminBorrowerController {

    @Resource
    private BorrowerService borrowerService;

    @ApiOperation("获取借款人列表")
    @GetMapping("list/{page}/{limit}")
    public R list(@ApiParam("页码") @PathVariable("page") Long page,
                  @ApiParam("每页条数") @PathVariable("limit") Long limit,
                  @ApiParam("查询条件对象") String keyword){

        Page<Borrower> borrowerPage = new Page<>(page,limit);
        IPage<Borrower> pageModel = borrowerService.listPage(borrowerPage,keyword);
        return R.ok().data("pageModel",pageModel);
    }

    @ApiOperation("根据ID获取借款人")
    @GetMapping("show/{id}")
    public R show(@ApiParam("借款人id") @PathVariable("id") Long id){

        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVO(id);

        return R.ok().data("borrowerDetailVO",borrowerDetailVO);
    }
}

