package com.app85soft.qiqishop.dto.request.category;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCategoryReq {
    @NotBlank
    String name;
    ActiveStatus status;
}
