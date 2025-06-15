package com.app85soft.qiqishop.dto.request.blog;

import com.app85soft.qiqishop.dto.constant.BlogStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddBlogReq {
    @NotBlank
    String title;
    Integer thumbnailId;
    @NotBlank
    String content;
    String description;
    BlogStatus status;
}
