package com.app85soft.qiqishop.dto.response.blog;

import com.app85soft.qiqishop.dto.constant.BlogStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogRes {
    int id;
    String title;
    int authorId;
    String authorName;
    int thumbnailId;
    String content;
    String description;

    String originUrl;
    String thumbUrl;

    BlogStatus status;
    Date createdAt;
}
