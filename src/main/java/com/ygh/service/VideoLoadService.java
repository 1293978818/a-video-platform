package com.ygh.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

/**
 * 处理视频上传的服务类接口
 * @author ygh
 */
public interface VideoLoadService {
    
    /**
     * 上传视频
     * @param url
     * @param file
     * @return
     * @throws IOException
     */
    public CompletableFuture<Boolean> loadVideo(String url,MultipartFile file) throws IOException;
}
