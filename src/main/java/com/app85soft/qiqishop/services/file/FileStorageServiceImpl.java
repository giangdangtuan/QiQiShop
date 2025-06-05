package com.app85soft.qiqishop.services.file;

import com.app85soft.qiqishop.entities.upload_file.UploadFile;
import com.app85soft.qiqishop.entities.upload_file.constant.UploadFileType;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.other_service.storage.StorageResource;
import com.app85soft.qiqishop.repositories.media.MediaRepository;
import com.app85soft.qiqishop.services.BaseService;
import com.app85soft.qiqishop.util.Constants;
import com.app85soft.qiqishop.util.Util;
import org.apache.commons.lang3.RandomStringUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
class FileStorageServiceImpl extends BaseService implements FileStorageService {

    @Autowired
    private StorageResource storageResource;

    @Autowired
    private MediaRepository mediaRepository;

    public UploadFile storeImage(final MultipartFile file) {
        String timeStamp = new SimpleDateFormat(Constants.YYYY_MM_DD_HH_mm_SSS).format(new Date());
        String randomString = RandomStringUtils.random(6, Constants.ALPHA_NUM);
        String fileName = Util.removeCharacterVn(StringUtils.cleanPath(file.getOriginalFilename().toLowerCase()));
        String originalName = timeStamp + "_" + randomString + "_" + fileName;
        String thumbName = timeStamp + "_" + randomString + "_thumb_" + fileName;
        if (originalName.contains("..")) {
            throw new BusinessException("Sorry! Filename contains invalid path sequence " + originalName);
        }
        String type = file.getContentType();
        if ((type == null || !type.toLowerCase().startsWith("image")) && !originalName.endsWith("jpg") && !originalName.endsWith("jpeg") && !originalName.endsWith("png")) {
            throw new BusinessException("File format error");
        }
        try {
            BufferedImage bimg = ImageIO.read(file.getInputStream());
            UploadFile image = new UploadFile();
            if (bimg != null) {
                image.setWidth(bimg.getWidth());
                image.setHeight(bimg.getHeight());
            }
            image.setType(UploadFileType.IMAGE);
            image.setSize(file.getSize());
            image.setOriginFilePath(String.format("image/%s", originalName));
            ByteArrayOutputStream thumbOutputStream = createThumbnail(file, type, fileName);
            image.setOriginUrl(storageResource.writeResource(file.getInputStream(), "image/" + originalName));
            if (thumbOutputStream != null) {
                try (InputStream inputStream = new ByteArrayInputStream(thumbOutputStream.toByteArray())) {
                    image.setThumbUrl(storageResource.writeResource(inputStream, "image/" + thumbName));
                    image.setThumbFilePath(String.format("image/%s", thumbName));
                }
            } else {
                image.setThumbUrl(image.getOriginUrl());
            }
            image = mediaRepository.save(image);
            return image;
        } catch (IOException exception) {
            throw new BusinessException(exception.getMessage());
        }

    }

    @Override
    public void deleteFile(int fileId) {
        UploadFile uploadFile = mediaRepository.findUploadFileById(fileId);
        if (uploadFile != null) {
            String fileName = uploadFile.getOriginFilePath();
            String thumbName = uploadFile.getThumbFilePath();
            if (StringUtils.hasText(fileName)) {
                storageResource.deleteFile(fileName);
            }
            if (StringUtils.hasText(thumbName)) {
                storageResource.deleteFile(thumbName);
            }
            mediaRepository.delete(uploadFile);
        }
    }

    public InputStream getInputStream(final String fileName) {
        return storageResource.readResource(fileName);
    }

    private ByteArrayOutputStream createThumbnail(final MultipartFile originalFile, String contentType, String fileName) {
        try {
            String formatType;
            if ((contentType != null && contentType.contains("png")) || fileName.contains("png")) {
                formatType = "png";
            } else {
                formatType = "jpeg";
            }
            ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
            BufferedImage img = ImageIO.read(originalFile.getInputStream());
            BufferedImage thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, Math.min(img.getWidth(), 1000), Scalr.OP_ANTIALIAS);
            ImageIO.write(thumbImg, formatType, thumbOutput);
            return thumbOutput;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
//@Service
//class FileStorageServiceImpl extends BaseService implements FileStorageService {
//
//    @Autowired
//    private StorageResource storageResource;
//
//    @Autowired
//    private MediaRepository mediaRepository;
//
//    private static final String IMAGE_FOLDER = "image/";
//
//    public UploadFile storeImage(final MultipartFile file) {
//        String timeStamp = new SimpleDateFormat(Constants.YYYY_MM_DD_HH_mm_SSS).format(new Date());
//        String randomString = RandomStringUtils.random(6, Constants.ALPHA_NUM);
//        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename().toLowerCase());
//        String safeFileName = cleanFileNamePreserveExtension(originalFileName);
//
//        String fileName = timeStamp + "_" + randomString + "_" + safeFileName;
//        String thumbName = timeStamp + "_" + randomString + "_thumb_" + safeFileName;
//
//        if (fileName.contains("..")) {
//            throw new BusinessException("Tên file không hợp lệ: " + fileName);
//        }
//
//        String type = file.getContentType();
//        if ((type == null || !type.toLowerCase().startsWith("image")) &&
//                !(fileName.endsWith("jpg") || fileName.endsWith("jpeg") || fileName.endsWith("png"))) {
//            throw new BusinessException("Định dạng file không được hỗ trợ");
//        }
//
//        try {
//            BufferedImage bimg = ImageIO.read(file.getInputStream());
//
//            UploadFile image = new UploadFile();
//            if (bimg != null) {
//                image.setWidth(bimg.getWidth());
//                image.setHeight(bimg.getHeight());
//            }
//
//            image.setType(UploadFileType.IMAGE);
//            image.setSize(file.getSize());
//            image.setOriginFilePath(IMAGE_FOLDER + fileName);
//
//            // Ghi file gốc
//            String originUrl = storageResource.writeResource(file.getInputStream(), IMAGE_FOLDER + fileName);
//            image.setOriginUrl(originUrl);
//
//            // Tạo thumbnail
//            ByteArrayOutputStream thumbOutputStream = createThumbnail(file, type, fileName);
//            if (thumbOutputStream != null) {
//                try (InputStream thumbInputStream = new ByteArrayInputStream(thumbOutputStream.toByteArray())) {
//                    String thumbUrl = storageResource.writeResource(thumbInputStream, IMAGE_FOLDER + thumbName);
//                    image.setThumbUrl(thumbUrl);
//                    image.setThumbFilePath(IMAGE_FOLDER + thumbName);
//                }
//            } else {
//                image.setThumbUrl(originUrl);
//            }
//
//            return mediaRepository.save(image);
//
//        } catch (IOException e) {
//            throw new BusinessException("Lỗi xử lý ảnh: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void deleteFile(int fileId) {
//        UploadFile uploadFile = mediaRepository.findUploadFileById(fileId);
//        if (uploadFile != null) {
//            if (StringUtils.hasText(uploadFile.getOriginFilePath())) {
//                storageResource.deleteFile(uploadFile.getOriginFilePath());
//            }
//            if (StringUtils.hasText(uploadFile.getThumbFilePath())) {
//                storageResource.deleteFile(uploadFile.getThumbFilePath());
//            }
//            mediaRepository.delete(uploadFile);
//        }
//    }
//
//    public InputStream getInputStream(final String fileName) {
//        return storageResource.readResource(fileName);
//    }
//
//    private ByteArrayOutputStream createThumbnail(final MultipartFile originalFile, String contentType, String fileName) {
//        try {
//            String formatType = (contentType != null && contentType.contains("png")) || fileName.contains("png") ? "png" : "jpeg";
//
//            BufferedImage img = ImageIO.read(originalFile.getInputStream());
//            BufferedImage thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC,
//                    Math.min(img.getWidth(), 1000), Scalr.OP_ANTIALIAS);
//
//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            ImageIO.write(thumbImg, formatType, output);
//            return output;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    // ✅ Hàm xử lý tên file, giữ lại phần mở rộng
//    private String cleanFileNamePreserveExtension(String fileName) {
//        if (fileName == null) return null;
//
//        int lastDot = fileName.lastIndexOf('.');
//        String namePart = (lastDot != -1) ? fileName.substring(0, lastDot) : fileName;
//        String extPart = (lastDot != -1) ? fileName.substring(lastDot) : "";
//
//        namePart = Normalizer.normalize(namePart, Normalizer.Form.NFD)
//                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
//                .replaceAll("[^a-zA-Z0-9-_]", "");
//
//        return namePart + extPart;
//    }
//}
