package com.app85soft.qiqishop.other_service.storage.ftp;

import com.app85soft.qiqishop.other_service.storage.StorageConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StorageFtpConfig implements StorageConfig {
    private String server;
    private int port;
    private String username;
    private String password;

    @Override
    public String toString() {
        return "ECFtpStorageConfig{" +
                "server='" + server + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                '}';
    }
}
