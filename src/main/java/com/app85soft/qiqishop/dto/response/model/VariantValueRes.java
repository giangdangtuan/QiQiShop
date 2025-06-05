package com.app85soft.qiqishop.dto.response.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantValueRes {
    Integer id;
    String value;
    Integer coverImage;
}
