package com.zmx.quickserver.service;

import com.zmx.quickserver.config.MinioConfig;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfig config;

    /**
     * 确保桶存在，如果不存在则创建
     */
    public void ensureBucketExists() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(config.getBucketName()).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(config.getBucketName()).build());
                log.info("Bucket '{}' created successfully.", config.getBucketName());
            } else {
                log.info("Bucket '{}' already exists.", config.getBucketName());
            }
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.error("Error ensuring bucket exists: " + e.getMessage(), e);
            throw new RuntimeException("Failed to ensure MinIO bucket exists.", e);
        }
    }

    /**
     * 上传文件
     * 
     * @param file MultipartFile 对象
     * @return 文件在MinIO中的对象名称
     */
    public String uploadFile(MultipartFile file) {
        ensureBucketExists(); // 确保桶存在
        String originalFilename = file.getOriginalFilename();
        String objectName = System.currentTimeMillis() + "-" + originalFilename; // 简单的生成唯一对象名称
        String url = config.getUrl() + "/" + config.getBucketName() + "/" + objectName;
        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(config.getBucketName())
                            .object(objectName)
                            .stream(is, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
            log.info("File '{}' uploaded to MinIO as '{}'.", originalFilename, objectName);
            return url;
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.error("Error uploading file: " + e.getMessage(), e);
            throw new RuntimeException("Failed to upload file to MinIO.", e);
        }
    }

    /**
     * 下载文件
     * 
     * @param objectName 文件在MinIO中的对象名称
     * @return 文件的输入流
     */
    public InputStream downloadFile(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(config.getBucketName())
                            .object(objectName)
                            .build());
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.error("Error downloading file: " + e.getMessage(), e);
            throw new RuntimeException("Failed to download file from MinIO.", e);
        }
    }

    /**
     * 删除文件
     * 
     * @param objectName 文件在MinIO中的对象名称
     */
    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(config.getBucketName())
                            .object(objectName)
                            .build());
            log.info("File '{}' deleted from MinIO.", objectName);
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.error("Error deleting file: " + e.getMessage(), e);
            throw new RuntimeException("Failed to delete file from MinIO.", e);
        }
    }

    /**
     * 检查文件是否存在
     * 
     * @param objectName 文件在MinIO中的对象名称
     * @return true 如果文件存在，否则 false
     */
    public boolean objectExists(String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(config.getBucketName())
                            .object(objectName)
                            .build());
            return true;
        } catch (MinioException e) {
            // 如果对象不存在，MinIOException 会被抛出，错误码通常是NoSuchKey
            log.error("Error checking object existence: " + e.getMessage(), e);
            throw new RuntimeException("Failed to check object existence in MinIO.", e);
        } catch (InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.error("Error checking object existence: " + e.getMessage(), e);
            throw new RuntimeException("Failed to check object existence in MinIO.", e);
        }
    }
}