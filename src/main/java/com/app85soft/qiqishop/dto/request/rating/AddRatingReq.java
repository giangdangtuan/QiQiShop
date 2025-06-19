package com.app85soft.qiqishop.dto.request.rating;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddRatingReq {
    @NotNull
    Integer orderId;

    List<RatingItem> ratingItems;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RatingItem {
        int productId;
        Integer ratingImage;
        String content;
        @Min(1)@Max(5)
        Integer ratingStar;
    }
}
