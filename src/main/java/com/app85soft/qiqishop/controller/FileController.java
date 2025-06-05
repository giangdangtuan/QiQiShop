package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.entities.upload_file.UploadFile;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.services.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("api/v1/media/upload-image")
    public ResponseEntity<BaseResponse<UploadFile>> uploadImage(@RequestParam("file") final MultipartFile file) {
        if (file == null) {
            throw new BusinessException(Translator.toLocale("required_fields"));
        }
        if (file.getSize() > 1024 * 1024 * 20) {
            throw new BusinessException("File size is too large, please choose file smaller than 20MB");
        }
        UploadFile uploadFile = fileStorageService.storeImage(file);
        return ResponseEntity.ok(new BaseResponse<>(uploadFile));
    }

//    @PostMapping("api/v1/media/upload-images")
//    public ResponseEntity<BaseResponse<List<UploadFile>>> uploadImages(@RequestParam("files") MultipartFile[] files) {
//        if (files == null || files.length == 0) {
//            throw new BusinessException(Translator.toLocale("required_fields"));
//        }
//
//        List<UploadFile> uploadFiles = Arrays.stream(files)
//                .peek(file -> {
//                    if (file.getSize() > 1024 * 1024 * 20) {
//                        throw new BusinessException("File size is too large, please choose file smaller than 20MB");
//                    }
//                })
//                .map(fileStorageService::storeImage)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(new BaseResponse<>(uploadFiles));
//    }

    @GetMapping("image/{fileName:.+}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable final String fileName) throws Exception {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(fileStorageService.getInputStream("image/" + fileName)));
    }

}

