package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.model.ModelRes;
import com.app85soft.qiqishop.dto.response.order.OrderRes;
import com.app85soft.qiqishop.services.model.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class ModelController {
    private final ModelService modelService;

    @Operation(summary = "Get model detail")
    @GetMapping("v1/model/detail/{id}")
    public ResponseEntity<BaseResponse<ModelRes>> getOrderDetail(@PathVariable("id") int id) {
        return ResponseEntity.ok(modelService.getModelDetail(id));
    }
}
