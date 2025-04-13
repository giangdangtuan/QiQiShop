package com.app85soft.qiqishop.dto.response.promotion;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
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
public class PromotionRes {
    int id;
    String name;
    Long startTime;
    Long endTime;
    ActiveStatus status;

    List<PromotionModelRes> promotionModels;
}
