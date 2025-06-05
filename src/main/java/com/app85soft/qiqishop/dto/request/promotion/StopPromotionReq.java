package com.app85soft.qiqishop.dto.request.promotion;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class StopPromotionReq {
    int promotionId;
}
