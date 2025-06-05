package com.app85soft.qiqishop.services.order;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.OrderStatus;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import com.app85soft.qiqishop.dto.request.ghn.GhnCreateOrderReq;
import com.app85soft.qiqishop.dto.request.ghn.GhnItemReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.ghn.GhnCreateOrderRes;
import com.app85soft.qiqishop.dto.response.order.OrderListRes;
import com.app85soft.qiqishop.dto.response.order.OrderRes;
import com.app85soft.qiqishop.entities.address.Address;
import com.app85soft.qiqishop.entities.model.Model;
import com.app85soft.qiqishop.entities.order.Order;
import com.app85soft.qiqishop.entities.order.OrderDetail;
import com.app85soft.qiqishop.entities.product.Product;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.external.GhnClient;
import com.app85soft.qiqishop.repositories.address.*;
import com.app85soft.qiqishop.repositories.model.ModelRepository;
import com.app85soft.qiqishop.repositories.order.OrderDetailRepository;
import com.app85soft.qiqishop.repositories.order.OrderRepository;
import com.app85soft.qiqishop.repositories.product.ProductRepository;
import com.app85soft.qiqishop.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends BaseService implements OrderService {
    private final GhnClient ghnClient;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final AddressRepository addressRepository;
    private final WardRepository wardRepository;
    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;
    private final ProductRepository productRepository;
    private final ModelRepository modelRepository;

    @Override
    public GhnCreateOrderRes createOrderGhn(int orderId) {
        User user = getUser(PermissionKey.CREATE, PermissionType.PRODUCT);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("order_not_found"));
        if (!order.getStatus().equals(OrderStatus.WAITING_FOR_CONFIMATION)) {
            throw new BusinessException("order_status_invalid");
        }
        Address address = addressRepository.findById(order.getAddressId())
                .orElseThrow(() -> new BusinessException("address_not_found"));
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);

        Map<Integer, Model> modelMap = orderDetails.stream()
                .map(item -> modelRepository.findById(item.getModelId())
                        .orElseThrow(() -> new BusinessException("model_not_found")))
                .collect(Collectors.toMap(Model::getId, m -> m));

        Map<Integer, Product> productMap = modelMap.values().stream()
                .map(model -> productRepository.findById(model.getProductId())
                        .orElseThrow(() -> new BusinessException("product_not_found")))
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<GhnItemReq> ghnItems = orderDetails.stream().map(item -> {
            Model model = modelMap.get(item.getModelId());
            Product product = productMap.get(model.getProductId());
            return GhnItemReq.builder()
                    .name(product.getName())
                    .code(model.getName())
                    .quantity(item.getAmount())
                    .weight(product.getWeight())
                    .build();
        }).collect(Collectors.toList());

        int totalWeight = orderDetails.stream().mapToInt(item -> {
            Model model = modelMap.get(item.getModelId());
            Product product = productMap.get(model.getProductId());
            return item.getAmount() * product.getWeight();
        }).sum();

        GhnCreateOrderReq orderRequest = GhnCreateOrderReq.builder()
                .paymentTypeId(1)
                .requiredNote("CHOXEMHANGKHONGTHU")
                .toName(address.getConsignee())
                .toPhone(address.getPhone())
                .toAddress(address.getDetailAddress())
                .toWardName(wardRepository.findById(address.getWardId())
                        .orElseThrow(() -> new BusinessException("ward_not_found")).getName())
                .toDistrictName(districtRepository.findById(address.getDistrictId())
                        .orElseThrow(() -> new BusinessException("district_not_found")).getName())
                .toProvinceName(provinceRepository.findById(address.getProvinceId())
                        .orElseThrow(() -> new BusinessException("province_not_found")).getName())
                .codAmount(order.getTotalPrice().intValue())
                .weight(totalWeight)
                .serviceTypeId(2)
                .items(ghnItems)
                .build();
        GhnCreateOrderRes ghnCreateOrderRes = ghnClient.createShippingOrder(orderRequest);
        order.setStatus(OrderStatus.PICKING);
        orderRepository.save(order);

        return ghnCreateOrderRes;
    }

    @Override
    public BaseResponse<List<OrderListRes>> getOrders(OrderStatus status, String orderCode, PaymentMethod paymentMethod, int page) {
        User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);
        long countOrder = orderRepository.countOrder(status, orderCode, paymentMethod);
        List<OrderListRes> listOrders = orderRepository.getOrders(status, orderCode, paymentMethod, null, page);
        return new BaseResponse<>(listOrders, countOrder, page);
    }

    @Override
    public BaseResponse<OrderRes> getOrder(String code) {
        User user = getUser();

        OrderRes orderRes = orderRepository.getOrder(code);
        if (orderRes == null) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.NOT_FOUND);
        }
        return new BaseResponse<>(orderRes);
    }

    @Override
    public BaseResponse<List<OrderListRes>> getMyOrders(OrderStatus status, String orderCode, PaymentMethod paymentMethod, int page) {
        User user = getUser();
        long countOrder = orderRepository.countOrder(status, orderCode, paymentMethod);
        List<OrderListRes> listOrders = orderRepository.getOrders(status, orderCode, paymentMethod, user.getId(), page);
        return new BaseResponse<>(listOrders, countOrder, page);
    }
}
