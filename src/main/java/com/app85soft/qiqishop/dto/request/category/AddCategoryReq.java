package com.app85soft.qiqishop.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
// @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddCategoryReq {
    @NotBlank
    String name;
}
