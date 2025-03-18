package com.app85soft.qiqishop.other_service.storage.s3;

import com.app85soft.qiqishop.other_service.storage.StorageConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StorageS3Config implements StorageConfig {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String region;

    @Override
    public String toString() {
        return "ECS3StorageConfig{" +
                "accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", bucket='" + bucket + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
