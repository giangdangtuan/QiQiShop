package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.promotion.AddPromotionReq;
import com.app85soft.qiqishop.dto.request.promotion.UpdatePromotionReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.promotion.PromotionRes;
import com.app85soft.qiqishop.services.promotion.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @Operation(summary = "Get list promotion.")
    @GetMapping("v1/promotion/list")
    public ResponseEntity<BaseResponse<List<PromotionRes>>> getPromotions(@RequestParam int page,
                                                                          @RequestParam(required = false) ActiveStatus status,
                                                                          @Parameter(description = "[name]")
                                                                          @RequestParam(required = false) String searchKeyword,
                                                                          @Parameter(description = "[startTime]")
                                                                          @RequestParam(required = false) Long startTime,
                                                                          @Parameter(description = "[endTime]")
                                                                          @RequestParam(required = false) Long endTime) {
        return ResponseEntity.ok(promotionService.getPromotions(status, searchKeyword, startTime, endTime, page));
    }


    @Operation(summary = "Add promotion")
    @PostMapping("v1/promotion/add")
    public ResponseEntity<BaseResponse<?>> addPromotion(@RequestBody @Valid AddPromotionReq req) {
        return ResponseEntity.ok(new BaseResponse<>(promotionService.addPromotion(req)));
    }

    @Operation(summary = "Update promotion")
    @PostMapping("v1/promotion/update")
    public ResponseEntity<BaseResponse<?>> updatePromotion(@RequestBody @Valid UpdatePromotionReq req) {
        return ResponseEntity.ok(new BaseResponse<>(promotionService.updatePromotion(req)));
    }

    @Operation(summary = "Delete promotions.")
    @PostMapping("v1/promotion/delete")
    public ResponseEntity<BaseResponse<List<Integer>>> deletePromotions(@RequestBody @Valid IdsRequest request) {
        return ResponseEntity.ok(new BaseResponse<>(promotionService.deletePromotions(request)));
    }

    @Operation(summary = "Get promotion detail")
    @GetMapping("v1/promotion/detail/{id}")
    public ResponseEntity<BaseResponse<PromotionRes>> getPromotionDetail(@PathVariable("id") int promotionId) {
        return ResponseEntity.ok(promotionService.getPromotion(promotionId));
    }
}
