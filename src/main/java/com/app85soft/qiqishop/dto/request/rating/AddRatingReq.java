package com.app85soft.qiqishop.dto.request.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddRatingReq {
    @NotNull
    Integer userId;
    @NotNull
    Integer orderId;
    @NotNull
    Integer modelId;
    Integer ratingImage;

    String content;
    @Min(1)@Max(5)
    Integer ratingStar;
}
