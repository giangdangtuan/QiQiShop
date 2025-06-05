package com.app85soft.qiqishop.dto.request.category;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddCategoryReq {
    @NotBlank
    String name;
    Integer coverImage;
    ActiveStatus status;
}
