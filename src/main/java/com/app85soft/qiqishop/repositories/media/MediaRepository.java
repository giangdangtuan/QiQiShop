package com.app85soft.qiqishop.repositories.media;

import com.app85soft.qiqishop.entities.upload_file.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<UploadFile, Integer> {
    UploadFile findUploadFileById(int id);
}
