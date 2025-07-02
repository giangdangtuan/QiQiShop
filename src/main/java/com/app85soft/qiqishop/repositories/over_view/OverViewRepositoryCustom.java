package com.app85soft.qiqishop.repositories.over_view;

import com.app85soft.qiqishop.dto.response.over_view.StatPoint;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public interface OverViewRepositoryCustom {
    BigDecimal getTotalProfit(Date fromDate, Date toDate);

    List<StatPoint> queryStatPoints(Date from, Date to, ChronoUnit unit, boolean isRevenue);
}
