package com.app85soft.qiqishop.dto.response.promotion;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionModelRes {
    int id;
    private int promotionId;
    private int modelId;
    private int discountPercentage;
    ActiveStatus status;
}
