package com.app85soft.qiqishop.dto.request.promotion;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddPromotionReq {
    @NotBlank
    String name;

    @NotNull
    Long startTime;

    @NotNull
    Long endTime;

    ActiveStatus status;

    List<PromotionModel> promotionModels;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PromotionModel {
        int promotionId;
        int modelId;
        int discountPercentage;
        ActiveStatus status;
    }
}
