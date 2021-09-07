package com.mosh.srb.core.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.mosh.common.exception.BusinessException;
import com.mosh.common.result.R;
import com.mosh.common.result.ResponseEnum;
import com.mosh.srb.core.pojo.dto.ExcelDictDTO;
import com.mosh.srb.core.pojo.entity.Dict;
import com.mosh.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author MoShuangHao
 * @since 2021-08-25
 */
//@CrossOrigin //通过gateway网关自动添加跨域配置
@Api("数据字典管理")
@RestController
@RequestMapping("/admin/core/dict")
@Slf4j
public class AdminDictController {

    @Resource
    private DictService dictService;

    @ApiOperation("根据上级id获取子节点数据列表")
    @GetMapping("/listByParentId/{parentId}")
    public R listByParentId(@ApiParam("上级节点") @PathVariable("parentId") Long parentId){
        List<Dict> list = dictService.listByParentId(parentId);
        return R.ok().data("list",list);
    }

    @ApiOperation("Excel批量导入数据字典")
    @PostMapping("/import")
    public R batchImport(@ApiParam("上传文件") @RequestParam("file") MultipartFile multipartFile){
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.ERROR);
        }

        dictService.importData(inputStream);
        return R.ok().message("文件上传成功");
    }

    @ApiOperation("数据字典导出Excel文件")
    @GetMapping("/export")
    public void export(HttpServletResponse response){

        try {
            List<ExcelDictDTO> list = dictService.listDictData();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("mydict", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-dispos`ition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), ExcelDictDTO.class).sheet("尚融宝数据字典").doWrite(list);
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.EXPORT_DATA_ERROR);
        }
    }

}

