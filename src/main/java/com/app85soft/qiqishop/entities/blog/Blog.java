package com.app85soft.qiqishop.entities.blog;

import com.app85soft.qiqishop.dto.constant.BlogStatus;
import com.app85soft.qiqishop.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "blogs")
public class Blog extends BaseEntity {
    String title;
    int authorId;
    Integer thumbnailId;
    String content;
    String description;
    BlogStatus status;
}
