package com.app85soft.qiqishop.repositories.order;

import com.app85soft.qiqishop.dto.constant.OrderStatus;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import com.app85soft.qiqishop.dto.response.order.OrderListRes;
import com.app85soft.qiqishop.dto.response.order.OrderRes;

import java.util.List;

public interface OrderRepositoryCustom {
    long countOrder(OrderStatus status, String orderCode, PaymentMethod paymentMethod);

    List<OrderListRes> getOrders(OrderStatus status, String orderCode, PaymentMethod paymentMethod, Integer userId, int page);

    OrderRes getOrder(String code);
}
