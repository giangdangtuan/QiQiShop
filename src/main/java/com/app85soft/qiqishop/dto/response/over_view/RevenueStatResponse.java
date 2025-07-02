package com.app85soft.qiqishop.dto.response.over_view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RevenueStatResponse {
    StatSummary confirmedGmv;
    StatSummary confirmedOrders;
    BigDecimal profit;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class StatSummary {
        BigDecimal value;
        double chainRatio;
        List<StatPoint> points;
        public StatSummary(BigDecimal revenueTotal, double revenueChainRatio, List<StatPoint> revenuePoints) {
            this.value = revenueTotal;
            this.chainRatio = revenueChainRatio;
            this.points = revenuePoints;
        }
    }
}
