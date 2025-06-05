package com.app85soft.qiqishop.dto.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantValueReq {
    Integer id;
    int variantOptionId;
    String value;
}
