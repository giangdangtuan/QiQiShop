package com.app85soft.qiqishop.other_service.storage;

import com.app85soft.qiqishop.dto.constant.StorageType;
import com.app85soft.qiqishop.other_service.storage.cloudinary.StorageCloudinary;
import com.app85soft.qiqishop.other_service.storage.cloudinary.StorageCloudinaryConfig;
import com.app85soft.qiqishop.other_service.storage.ftp.StorageFtp;
import com.app85soft.qiqishop.other_service.storage.ftp.StorageFtpConfig;
import com.app85soft.qiqishop.other_service.storage.nfs.StorageLocal;
import com.app85soft.qiqishop.other_service.storage.nfs.StorageNfsConfig;
import com.app85soft.qiqishop.other_service.storage.s3.StorageS3;
import com.app85soft.qiqishop.other_service.storage.s3.StorageS3Config;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Log4j2
@Configuration
public class FileStorageConfig {

    @Value("${file.storage-type}")
    private String storageType;

    @Autowired
    protected HttpServletRequest servletRequest;

    @Autowired
    private Environment environment;

    @Bean
    public StorageResource storageResource() {
        StorageType type = StorageType.getType(storageType);
        switch (type) {
            case STORAGE_S3 -> {
                String accessKey = environment.getProperty("s3.access.key");
                String secretKey = environment.getProperty("s3.secret.key");
                String bucket = environment.getProperty("s3.bucket");
                String region = environment.getProperty("s3.region");
                return new StorageS3(new StorageS3Config(accessKey, secretKey, bucket, region));
            }
            case STORAGE_AZURE, STORAGE_GCP -> throw new UnsupportedOperationException("Not implement this storage");
            case STORAGE_FTP -> {
                String server = environment.getProperty("file.ftp.server");
                String port = environment.getProperty("file.ftp.port");
                String username = environment.getProperty("file.ftp.username");
                String password = environment.getProperty("file.ftp.password");
                return new StorageFtp(new StorageFtpConfig(server, Integer.parseInt(port), username, password));
            }
            case STORAGE_CLOUDINARY -> {
                String cloudName = environment.getProperty("cloudinary.cloud-name");
                String apiKey = environment.getProperty("cloudinary.api-key");
                String apiSecret = environment.getProperty("cloudinary.api-secret");
                return new StorageCloudinary(new StorageCloudinaryConfig(cloudName, apiKey, apiSecret));
            }
            default -> {
                String directory = environment.getProperty("file.upload-dir");
                String serverUrl = environment.getProperty("system.backend.url");
                return new StorageLocal(new StorageNfsConfig(directory, serverUrl));
            }
        }
    }
}
