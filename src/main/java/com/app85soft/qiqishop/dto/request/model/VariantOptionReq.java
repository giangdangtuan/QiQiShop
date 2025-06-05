package com.app85soft.qiqishop.dto.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantOptionReq {
    Integer id;
    int productId;
    String name;
    List<VariantValueReq> values;
}
