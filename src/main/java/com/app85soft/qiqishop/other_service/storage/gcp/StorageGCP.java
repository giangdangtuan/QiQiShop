package com.app85soft.qiqishop.other_service.storage.gcp;

import com.app85soft.qiqishop.other_service.storage.StorageResource;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;

@Log4j2
public class StorageGCP implements StorageResource {
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
}
