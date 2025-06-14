package com.app85soft.qiqishop.services.payment;

import com.app85soft.qiqishop.dto.request.payment.CheckOutReq;
import com.app85soft.qiqishop.dto.request.payment.ConfirmCheckOutReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.payment.CheckOutRes;
import com.app85soft.qiqishop.dto.response.payment.ConfirmCheckOutRes;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    BaseResponse<CheckOutRes> checkOut(CheckOutReq req);
    BaseResponse<ConfirmCheckOutRes> confirmCheckOut(ConfirmCheckOutReq req);
    void handleVnPaySuccess(String orderCode, int addressId, BigDecimal totalAmount, List<Integer> cartItemIds, int userId, String referenceCode, long payDate, String note, BigDecimal shippingCost);
}
