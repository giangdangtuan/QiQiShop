package com.app85soft.qiqishop.other_service.storage.azure;

import com.app85soft.qiqishop.other_service.storage.StorageResource;

import java.io.InputStream;

public class StorageAzure implements StorageResource {
    @Override
    public InputStream readResource(String path) {
        return null;
    }

    @Override
    public String writeResource(InputStream inputStream, String path) {
        return null;
    }

    @Override
    public boolean deleteFile(String file) {
        return false;
    }

    @Override
    public String getUrl(String file) {
        return null;
    }

    @Override
    public String getPublicUrl(String path) {
        return "";
    }


}
