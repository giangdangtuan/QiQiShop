package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.constant.OrderStatus;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import com.app85soft.qiqishop.dto.request.order.OrderChangeStatusReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.ghn.GhnCreateOrderRes;
import com.app85soft.qiqishop.dto.response.order.OrderListRes;
import com.app85soft.qiqishop.dto.response.order.OrderRes;
import com.app85soft.qiqishop.entities.order.Order;
import com.app85soft.qiqishop.services.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/v1/order/to-picking")
    public ResponseEntity<BaseResponse<GhnCreateOrderRes>> createGhnOrder(@RequestBody @Valid OrderChangeStatusReq req) {
        return ResponseEntity.ok(new BaseResponse<>(orderService.createOrderGhn(req)));
    }

    @Operation(summary = "Get list order.")
    @GetMapping("v1/order/list")
    public ResponseEntity<BaseResponse<List<OrderListRes>>> getOrders(@RequestParam int page,
                                                                      @RequestParam(required = false) OrderStatus status,
                                                                      @RequestParam(required = false) String orderCode,
                                                                      @RequestParam(required = false) PaymentMethod method) {
        return ResponseEntity.ok(orderService.getOrders(status, orderCode, method, page));
    }

    @Operation(summary = "Get order detail")
    @GetMapping("v1/order/detail/{id}")
    public ResponseEntity<BaseResponse<OrderRes>> getOrderDetail(@PathVariable("id") int orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @Operation(summary = "Cancel order")
    @PostMapping("/v1/order/cancel")
    public ResponseEntity<BaseResponse<Order>> cancelOrder(@RequestBody @Valid OrderChangeStatusReq req) {
        return ResponseEntity.ok(orderService.CancelOrder(req));
    }

    //    -----------USER-----------
    @Operation(summary = "Get list order for user.")
    @GetMapping("v1/my-order/list")
    public ResponseEntity<BaseResponse<List<OrderListRes>>> getOrdersByUserId(@RequestParam int page,
                                                                      @RequestParam(required = false) OrderStatus status,
                                                                      @RequestParam(required = false) String orderCode,
                                                                      @RequestParam(required = false) PaymentMethod method) {
        return ResponseEntity.ok(orderService.getMyOrders(status, orderCode, method, page));
    }
}
