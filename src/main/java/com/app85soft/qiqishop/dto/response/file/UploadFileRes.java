package com.app85soft.qiqishop.dto.response.file;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadFileRes {
    String originUrl;
    String thumbUrl;
}
