package com.app85soft.qiqishop.services.over_view;

import com.app85soft.qiqishop.dto.response.over_view.RevenueStatResponse;

public interface OverViewService {

    RevenueStatResponse getRevenueStat(long startUnix, long endUnix, String period);
}
