package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.over_view.RevenueStatResponse;
import com.app85soft.qiqishop.services.over_view.OverViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class OverViewController {
    private final OverViewService overViewService;

    @GetMapping("v1/revenue-stat")
    public ResponseEntity<BaseResponse<RevenueStatResponse>> getRevenueStat(
            @RequestParam long startTime,
            @RequestParam long endTime,
            @RequestParam String period) {

        return ResponseEntity.ok(new BaseResponse<>(overViewService.getRevenueStat(startTime, endTime, period)));
    }

}
