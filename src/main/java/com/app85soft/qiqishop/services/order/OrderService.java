package com.app85soft.qiqishop.services.order;

import com.app85soft.qiqishop.dto.constant.OrderStatus;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.ghn.GhnCreateOrderRes;
import com.app85soft.qiqishop.dto.response.order.OrderListRes;
import com.app85soft.qiqishop.dto.response.order.OrderRes;

import java.util.List;

public interface OrderService {
    GhnCreateOrderRes createOrderGhn(int orderId);

    BaseResponse<List<OrderListRes>> getOrders(OrderStatus status, String orderCode, PaymentMethod paymentMethod, int page);

    BaseResponse<OrderRes> getOrder(String code);


    //    -----------USER-----------
    BaseResponse<List<OrderListRes>> getMyOrders(OrderStatus status, String orderCode, PaymentMethod paymentMethod, int page);
}
