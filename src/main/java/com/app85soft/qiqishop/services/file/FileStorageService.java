package com.app85soft.qiqishop.services.file;

import com.app85soft.qiqishop.entities.upload_file.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileStorageService {

    UploadFile storeImage(final MultipartFile file);
    void deleteFile(int fileId);
    InputStream getInputStream(final String fileName);
}
