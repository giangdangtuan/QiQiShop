package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.request.rating.AddRatingReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.rating.RatingRes;
import com.app85soft.qiqishop.services.rating.RatingService;
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
public class RatingController {
    private final RatingService ratingService;

    @Operation(summary = "Add RatingOrder")
    @PostMapping("v1/rating/add")
    public ResponseEntity<BaseResponse<?>> addRating(@RequestBody @Valid AddRatingReq req) {
        return ResponseEntity.ok(new BaseResponse<>(ratingService.addRating(req)));
    }

    @Operation(summary = "Get list Rating.")
    @GetMapping("rating/list")
    public ResponseEntity<BaseResponse<List<RatingRes>>> getRatings(@RequestParam int page,
                                                                    @Parameter(description = "[productId]")
                                                                    @RequestParam int productId) {
        return ResponseEntity.ok(ratingService.getRatings(productId, page));
    }
}
