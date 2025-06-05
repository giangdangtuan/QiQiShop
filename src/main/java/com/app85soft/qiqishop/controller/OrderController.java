package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.constant.OrderStatus;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.ghn.GhnCreateOrderRes;
import com.app85soft.qiqishop.dto.response.order.OrderListRes;
import com.app85soft.qiqishop.dto.response.order.OrderRes;
import com.app85soft.qiqishop.dto.response.product.ProductRes;
import com.app85soft.qiqishop.services.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/v1/order/to-picking/{orderId}")
    public ResponseEntity<BaseResponse<GhnCreateOrderRes>> createGhnOrder(@PathVariable int orderId) {
        return ResponseEntity.ok(new BaseResponse<>(orderService.createOrderGhn(orderId)));
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
    @GetMapping("v1/order/detail/{code}")
    public ResponseEntity<BaseResponse<OrderRes>> getOrderDetail(@PathVariable("code") String code) {
        return ResponseEntity.ok(orderService.getOrder(code));
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
