package com.mosh.srb.oss.controller;

import com.mosh.common.exception.BusinessException;
import com.mosh.common.result.R;
import com.mosh.common.result.ResponseEnum;
import com.mosh.srb.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/9/7
 */
@Api(tags = "阿里云文件管理")
@RestController
@RequestMapping("/api/oss/file")
public class FileController {

    @Resource
    private FileService fileService;

    /**
     * 文件上传
     */
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public R upload(@ApiParam("文件") @RequestParam("file") MultipartFile file,
                    @ApiParam("模块") @RequestParam("module") String module){
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String uploadUrl = fileService.upload(inputStream,module,originalFilename);

            return R.ok().message("文件上传成功").data("url",uploadUrl);
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR,e);
        }

    }

    @ApiOperation("文件删除")
    @DeleteMapping("/remove")
    public R remove(@ApiParam("文件路径") @RequestParam("url") String url){
        fileService.remove(url);
        return R.ok().message("文件删除成功");
    }

}
