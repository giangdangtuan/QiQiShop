package com.app85soft.qiqishop.other_service.storage.cloudinary;

import com.app85soft.qiqishop.other_service.storage.StorageConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StorageCloudinaryConfig implements StorageConfig {
    private String cloudName;
    private String apiKey;
    private String apiSecret;

    @Override
    public String toString() {
        return "StorageCloudinaryConfig{" +
                "cloudName='" + cloudName + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", apiSecret='" + apiSecret + '\'' +
                '}';
    }
}
