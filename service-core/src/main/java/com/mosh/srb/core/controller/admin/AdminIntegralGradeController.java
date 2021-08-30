package com.mosh.srb.core.controller.admin;

import com.mosh.common.exception.Assert;
import com.mosh.common.exception.BusinessException;
import com.mosh.common.result.R;
import com.mosh.common.result.ResponseEnum;
import com.mosh.srb.core.pojo.entity.IntegralGrade;
import com.mosh.srb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/8/25
 */
@Api("积分等级管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {
    @Resource
    private IntegralGradeService integralGradeService;

    @ApiOperation("积分等级列表")
    @GetMapping("/list")
    public R list(){
        List<IntegralGrade> list = integralGradeService.list();
        return R.ok().data("list",list);
    }

    @ApiOperation("根据Id查找积分等级")
    @GetMapping("/get/{id}")
    public R getById(@ApiParam("积分等级ID") @PathVariable Long id){
        IntegralGrade integralGrade = integralGradeService.getById(id);

        Assert.notNull(integralGrade,ResponseEnum.ERROR);

        return R.ok().data("record",integralGrade);
    }

    @ApiOperation("保存积分等级")
    @PostMapping("/save")
    public R save(@ApiParam("积分对象") @RequestBody IntegralGrade integralGrade){
        //断言，如果借款额度大于0，不执行isTrue
        Assert.isTrue(integralGrade.getBorrowAmount().intValue()>0,ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
        Assert.notNull(integralGrade.getBorrowAmount(),ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
        boolean save = integralGradeService.save(integralGrade);

        Assert.isTrue(save,ResponseEnum.ERROR);

        return R.ok().message("保存成功");
    }

    @ApiOperation("删除积分等级")
    @DeleteMapping("/remove/{id}")
    public R delete(@ApiParam("积分等级ID") @PathVariable Long id){
        boolean b = integralGradeService.removeById(id);
        Assert.isTrue(b,ResponseEnum.ERROR);
        return R.ok().message("删除成功");
    }

    @ApiOperation("更新积分等级")
    @PutMapping("/update")
    public R update(@ApiParam("积分对象") @RequestBody IntegralGrade integralGrade){
        boolean b = integralGradeService.updateById(integralGrade);
        Assert.isTrue(b,ResponseEnum.ERROR);
        return R.ok().message("修改成功");
    }
}
