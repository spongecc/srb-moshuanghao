package com.mosh.srb.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.mosh.srb.oss.service.FileService;
import com.mosh.srb.oss.util.OssProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/9/8
 */
@SpringBootTest
public class TestUpload {

    @Resource
    private FileService fileService;

    @Test
    public void a() throws FileNotFoundException {
        OSS ossClient = new OSSClientBuilder().build(
                OssProperties.ENDPOINT,
                OssProperties.KEY_ID,
                OssProperties.KEY_SECRET
        );

        boolean b = ossClient.doesBucketExist(OssProperties.BUCKET_NAME);
        System.out.println("b = " + b);
        //ossClient.putObject(OssProperties.BUCKET_NAME,key,inputStream);

        String kuyUrl = "module/2021/9/1/" + "uuid" + ".jpg";
        ossClient.putObject(OssProperties.BUCKET_NAME,kuyUrl,new FileInputStream(new File("C:\\Users\\moshuanghao\\Pictures\\idea.jpg")));
        ossClient.shutdown();
    }

}
