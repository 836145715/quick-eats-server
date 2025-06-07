package com.zmx.quickserver.controller.admin;

import com.zmx.common.response.Result;
import com.zmx.quickserver.service.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@Tag(name = "文件管理", description = "文件上传下载接口")
@Slf4j
public class FileController {

    @Autowired
    private MinioService minioService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件到MinIO")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        try {
            String objectName = minioService.uploadFile(file);
            // 通常会返回文件的访问URL，这里只返回对象名称
            return Result.success(objectName);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    // 您可以添加下载、删除等其他接口
}