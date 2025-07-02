package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.constant.OrderStatus;
import com.app85soft.qiqishop.dto.constant.PaymentGateway;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import com.app85soft.qiqishop.dto.constant.TransactionStatus;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.order.OrderListRes;
import com.app85soft.qiqishop.dto.response.order.OrderRes;
import com.app85soft.qiqishop.dto.response.transaction.TransactionRes;
import com.app85soft.qiqishop.services.transaction.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(summary = "Get list transaction.")
    @GetMapping("v1/transaction/list")
    public ResponseEntity<BaseResponse<List<TransactionRes>>> getTransactions(@RequestParam int page,
                                                                        @RequestParam(required = false) TransactionStatus status,
                                                                              @RequestParam(required = false) PaymentGateway method,
                                                                        @RequestParam(required = false) String searchKeyword) {
        return ResponseEntity.ok(transactionService.getTransactions(status, method, searchKeyword, page));
    }

    @Operation(summary = "Get transaction detail")
    @GetMapping("v1/transaction/detail/{id}")
    public ResponseEntity<BaseResponse<TransactionRes>> getTransactionDetail(@PathVariable("id") int transactionId) {
        return ResponseEntity.ok(transactionService.getTransaction(transactionId));
    }
}
