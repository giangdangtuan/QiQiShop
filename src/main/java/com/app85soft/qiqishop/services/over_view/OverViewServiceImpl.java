package com.app85soft.qiqishop.services.over_view;

import com.app85soft.qiqishop.dto.response.over_view.RevenueStatResponse;
import com.app85soft.qiqishop.dto.response.over_view.StatPoint;
import com.app85soft.qiqishop.repositories.over_view.OverViewRepositoryCustom;
import com.app85soft.qiqishop.services.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OverViewServiceImpl extends BaseService implements OverViewService {
    private final OverViewRepositoryCustom overViewRepository;

    @Override
    public RevenueStatResponse getRevenueStat(long startUnix, long endUnix, String period) {
        Date from = Date.from(Instant.ofEpochSecond(startUnix));
        Date to = Date.from(Instant.ofEpochSecond(endUnix));

        // Xác định đơn vị thống kê theo period
        ChronoUnit unit = switch (period.toLowerCase()) {
            case "day" -> ChronoUnit.HOURS;
            case "week", "month" -> ChronoUnit.DAYS;
            case "year" -> ChronoUnit.MONTHS;
            default -> throw new IllegalArgumentException("Invalid period: " + period);
        };

        // Lấy dữ liệu kỳ hiện tại
        List<StatPoint> revenuePoints = overViewRepository.queryStatPoints(from, to, unit, true);
        List<StatPoint> orderPoints = overViewRepository.queryStatPoints(from, to, unit, false);

        BigDecimal revenueTotal = revenuePoints.stream()
                .map(StatPoint::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal orderTotal = new BigDecimal(orderPoints.stream()
                .mapToLong(p -> p.getValue().longValue()).sum());

        // Tính prevFrom và prevTo theo period thực tế
        ZonedDateTime zFrom = from.toInstant().atZone(zoneId());
        ZonedDateTime zTo = to.toInstant().atZone(zoneId());

        ZonedDateTime prevFromZdt, prevToZdt;

        switch (period.toLowerCase()) {
            case "day" -> {
                prevFromZdt = zFrom.minusDays(1);
                prevToZdt = zTo.minusDays(1);
            }
            case "week" -> {
                prevFromZdt = zFrom.minusWeeks(1);
                prevToZdt = zTo.minusWeeks(1);
            }
            case "month" -> {
                prevFromZdt = zFrom.minusMonths(1).withDayOfMonth(1);
                prevToZdt = prevFromZdt.plusMonths(1).minusNanos(1); // cuối tháng
            }
            case "year" -> {
                prevFromZdt = zFrom.minusYears(1).withDayOfYear(1);
                prevToZdt = prevFromZdt.plusYears(1).minusNanos(1); // cuối năm
            }
            default -> throw new IllegalArgumentException("Invalid period: " + period);
        }

        Date prevFrom = Date.from(prevFromZdt.toInstant());
        Date prevTo = Date.from(prevToZdt.toInstant());

        // Lấy dữ liệu kỳ trước
        List<StatPoint> prevRevenuePoints = overViewRepository.queryStatPoints(prevFrom, prevTo, unit, true);
        List<StatPoint> prevOrderPoints = overViewRepository.queryStatPoints(prevFrom, prevTo, unit, false);

        BigDecimal prevRevenueTotal = prevRevenuePoints.stream()
                .map(StatPoint::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal prevOrderTotal = new BigDecimal(prevOrderPoints.stream()
                .mapToLong(p -> p.getValue().longValue()).sum());

        double revenueChainRatio = calcRatio(revenueTotal, prevRevenueTotal);
        double orderChainRatio = calcRatio(orderTotal, prevOrderTotal);

        return new RevenueStatResponse(
                new RevenueStatResponse.StatSummary(revenueTotal, revenueChainRatio, revenuePoints),
                new RevenueStatResponse.StatSummary(orderTotal, orderChainRatio, orderPoints),
                overViewRepository.getTotalProfit(from, to)
        );
    }

    private java.time.ZoneId zoneId() {
        return java.time.ZoneId.systemDefault(); // hoặc "Asia/Ho_Chi_Minh" nếu cố định
    }


    private double calcRatio(BigDecimal current, BigDecimal previous) {
        if (previous.compareTo(BigDecimal.ZERO) == 0) return 0.0;

        double ratio = (100 - (current.doubleValue() / previous.doubleValue() * 100)) * (-1);

        return BigDecimal.valueOf(ratio)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
