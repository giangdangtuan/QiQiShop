package com.app85soft.qiqishop.dto.request.category;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCategoryReq extends AddCategoryReq{
    @NotNull
    int CategoryId;
}
