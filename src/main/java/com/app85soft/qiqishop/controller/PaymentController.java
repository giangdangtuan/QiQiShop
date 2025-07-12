package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.annotations.NoRequireAuth;
import com.app85soft.qiqishop.dto.request.payment.CheckOutReq;
import com.app85soft.qiqishop.dto.request.payment.ConfirmCheckOutReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.payment.CheckOutRes;
import com.app85soft.qiqishop.dto.response.payment.ConfirmCheckOutRes;
import com.app85soft.qiqishop.services.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Check Out")
    @PostMapping("v1/checkout")
    public ResponseEntity<BaseResponse<CheckOutRes>> checkOut(@RequestBody CheckOutReq req) {
        return ResponseEntity.ok(paymentService.checkOut(req));
    }

    @Operation(summary = "ConfirmCheck check Out")
    @PostMapping("v1/confirm-checkout")
    public ResponseEntity<BaseResponse<ConfirmCheckOutRes>> confirmCheckOut(@RequestBody ConfirmCheckOutReq req) {
        return ResponseEntity.ok(paymentService.confirmCheckOut(req));
    }

    @NoRequireAuth
    @GetMapping("/v1/vnpay-return")
    public RedirectView vnpayReturn(@RequestParam Map<String, String> params, HttpServletRequest request) {
        String queryString = request.getQueryString();
        String redirectUrl = "https://qi-qi-shop-fe.vercel.app/checkout/status";
        if (queryString != null) {
            redirectUrl += "?" + queryString;
        }
        String responseCode = params.get("vnp_ResponseCode");

        if ("00".equals(responseCode)) {
            try {
                String orderInfo = params.get("vnp_OrderInfo");

                Map<String, String> infoMap = Arrays.stream(orderInfo.split("\\|"))
                        .map(pair -> pair.split("=", 2))
                        .filter(pair -> pair.length == 2)
                        .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));

                String orderCode = infoMap.get("orderCode");
                String note = infoMap.get("note");
                BigDecimal shippingCost = new BigDecimal(infoMap.get("shippingCost").replace(",", "."));
                log.info("VNPay amount: " + shippingCost);
                List<Integer> cartItemIds = Arrays.stream(infoMap.get("cart").split(","))
                        .map(Integer::parseInt)
                        .toList();
                int userId = Integer.parseInt(infoMap.get("userId"));
                int addressId = Integer.parseInt(infoMap.get("addressId"));

                String referenceCode = params.get("vnp_TxnRef");
                String payDateStr = params.get("vnp_PayDate");

                long payDate = convertVnPayDateToTimestamp(payDateStr);

                BigDecimal totalAmount = new BigDecimal(params.get("vnp_Amount"))
                        .divide(BigDecimal.valueOf(100));
                paymentService.handleVnPaySuccess(orderCode, addressId, totalAmount, cartItemIds, userId, referenceCode, payDate, note, shippingCost);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new RedirectView(redirectUrl);
    }

    private long convertVnPayDateToTimestamp(String payDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime localDateTime = LocalDateTime.parse(payDateStr, formatter);
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
