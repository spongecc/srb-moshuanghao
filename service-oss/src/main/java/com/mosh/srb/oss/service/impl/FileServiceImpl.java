package com.mosh.srb.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.mosh.common.exception.Assert;
import com.mosh.common.exception.BusinessException;
import com.mosh.common.result.ResponseEnum;
import com.mosh.srb.oss.service.FileService;
import com.mosh.srb.oss.util.OssProperties;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/9/7
 */
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(InputStream inputStream, String module, String originalFilename) {

        OSS ossClient = new OSSClientBuilder().build(
                OssProperties.ENDPOINT,
                OssProperties.KEY_ID,
                OssProperties.KEY_SECRET
        );
        //判断oss实例是否存在：如果不存在则创建，如果存在则获取
        if (!ossClient.doesBucketExist(OssProperties.BUCKET_NAME)){
            //创建bucket
            ossClient.createBucket(OssProperties.BUCKET_NAME);
            ossClient.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
        }

        String folder = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

        originalFilename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        String key = module + "/" + folder + "/" +originalFilename;

        ossClient.putObject(OssProperties.BUCKET_NAME,key,inputStream);

        ossClient.shutdown();

        String filePath = "https://" + OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/" + key;

        return filePath;
    }

    @Override
    public void remove(String url) {
        OSS ossClient = new OSSClientBuilder().build(
                OssProperties.ENDPOINT,
                OssProperties.KEY_ID,
                OssProperties.KEY_SECRET
        );
        //判断oss实例是否存在
        Assert.isTrue(ossClient.doesBucketExist(OssProperties.BUCKET_NAME),ResponseEnum.UPLOAD_ERROR);

        String host = "https://" + OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/";
        String substring = url.substring(host.length());

        try {
            ossClient.deleteObject(OssProperties.BUCKET_NAME,substring);
        } catch (OSSException e) {
            throw new BusinessException(ResponseEnum.ALIYUN_RESPONSE_FAIL,e);
        }
        ossClient.shutdown();
    }
}
