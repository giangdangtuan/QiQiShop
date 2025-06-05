package com.app85soft.qiqishop.dto.response.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantOptionRes {
    int id;
    String name;
    List<VariantValueRes> values;
}
