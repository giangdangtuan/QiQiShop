package com.app85soft.qiqishop.services.order;

import com.app85soft.qiqishop.dto.constant.OrderStatus;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import com.app85soft.qiqishop.dto.request.order.OrderChangeStatusReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.ghn.GhnCreateOrderRes;
import com.app85soft.qiqishop.dto.response.order.OrderListRes;
import com.app85soft.qiqishop.dto.response.order.OrderRes;
import com.app85soft.qiqishop.entities.order.Order;

import java.util.List;

public interface OrderService {
    GhnCreateOrderRes createOrderGhn(OrderChangeStatusReq req);

    BaseResponse<List<OrderListRes>> getOrders(OrderStatus status, String orderCode, PaymentMethod paymentMethod, int page);

    BaseResponse<OrderRes> getOrder(int orderId);

    BaseResponse<Order> CancelOrder(OrderChangeStatusReq req);

    BaseResponse<Order> CompleteOrder(OrderChangeStatusReq req);


    //    -----------USER-----------
    BaseResponse<List<OrderListRes>> getMyOrders(OrderStatus status, String orderCode, PaymentMethod paymentMethod, int page);
}
