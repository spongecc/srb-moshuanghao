package com.mosh.srb.oss.service;

import java.io.InputStream;

/**
 * @author 莫双豪
 * @version 1.0
 * @date 2021/9/7
 */

public interface FileService {

    String upload(InputStream inputStream, String module, String originalFilename);

    void remove(String url);
}
