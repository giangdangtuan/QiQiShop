package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.request.purchase_order.AddPurchaseOrderReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.purchase_order.PurchaseOrderRes;
import com.app85soft.qiqishop.services.purchase_order.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    @Operation(summary = "Add PurchaseOrder")
    @PostMapping("v1/purchase-order/add")
    public ResponseEntity<BaseResponse<?>> addPurchaseOrder(@RequestBody @Valid AddPurchaseOrderReq req) {
        return ResponseEntity.ok(new BaseResponse<>(purchaseOrderService.addPurchaseOrder(req)));
    }

    @Operation(summary = "Get list PurchaseOrder.")
    @GetMapping("v1/purchase-order/list")
    public ResponseEntity<BaseResponse<List<PurchaseOrderRes>>> getPurchaseOrders(@RequestParam int page,
                                                                                  @Parameter(description = "[code]")
                                                                                  @RequestParam(required = false) String searchKeyword,
                                                                                  @Parameter(description = "[startTime]")
                                                                                  @RequestParam(required = false) Date startTime,
                                                                                  @Parameter(description = "[endTime]")
                                                                                  @RequestParam(required = false) Date endTime) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrders(searchKeyword, startTime, endTime, page));
    }

    @Operation(summary = "Get PurchaseOrder detail")
    @GetMapping("v1/purchase-order/detail/{id}")
    public ResponseEntity<BaseResponse<PurchaseOrderRes>> getPurchaseOrderDetail(@PathVariable("id") int purchaseOrderId) {
        return ResponseEntity.ok(purchaseOrderService.getPurchaseOrder(purchaseOrderId));
    }
}
