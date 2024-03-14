package com.ygh.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ygh.service.VideoLoadService;

/**
 * @author ygh
 */
@Service
public class VideoLoadServiceImpl implements VideoLoadService{

    @Async
    @Override
    public CompletableFuture<Boolean> loadVideo(String url, MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        Path path = Paths.get(url);
        Files.write(path, bytes);
        return CompletableFuture.completedFuture(true);
    }
    
}
