package com.app85soft.qiqishop.dto.response.product;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.model.ModelRes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductRes {
    int id;
    String code;
    String name;
    Integer categoryId;
    Integer coverImage;
    String description;
    ActiveStatus status;

    List<ModelRes> models;
}
