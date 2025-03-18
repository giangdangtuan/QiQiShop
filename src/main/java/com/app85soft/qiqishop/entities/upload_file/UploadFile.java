package com.app85soft.qiqishop.entities.upload_file;

import com.app85soft.qiqishop.entities.BaseEntity;
import com.app85soft.qiqishop.entities.upload_file.constant.UploadFileType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "upload_files")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadFile extends BaseEntity {

    private String originFilePath;
    private String thumbFilePath;
    private String originUrl;
    private String thumbUrl;
    @Column(columnDefinition = "int")
    private UploadFileType type;
    private Integer width;
    private Integer height;
    private Integer duration;
    private Long size;

}
