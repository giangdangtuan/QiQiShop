package com.app85soft.qiqishop.services.payment;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.OrderStatus;
import com.app85soft.qiqishop.dto.constant.PaymentGateway;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import com.app85soft.qiqishop.dto.constant.TransactionStatus;
import com.app85soft.qiqishop.dto.request.EmailDetail;
import com.app85soft.qiqishop.dto.request.order.OrderReq;
import com.app85soft.qiqishop.dto.request.payment.CheckOutReq;
import com.app85soft.qiqishop.dto.request.payment.ConfirmCheckOutReq;
import com.app85soft.qiqishop.dto.request.transaction.TransactionReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.address.AddressRes;
import com.app85soft.qiqishop.dto.response.cart.CartRes;
import com.app85soft.qiqishop.dto.response.payment.CheckOutRes;
import com.app85soft.qiqishop.dto.response.payment.ConfirmCheckOutRes;
import com.app85soft.qiqishop.entities.order.Order;
import com.app85soft.qiqishop.entities.order.OrderDetail;
import com.app85soft.qiqishop.entities.order.OrderDetailBatch;
import com.app85soft.qiqishop.entities.purchase_orders.StockBatch;
import com.app85soft.qiqishop.entities.transaction.Transactions;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.external.GhnClient;
import com.app85soft.qiqishop.other_service.send_email.SendEmailService;
import com.app85soft.qiqishop.repositories.address.AddressRepository;
import com.app85soft.qiqishop.repositories.cart_item.CartItemRepository;
import com.app85soft.qiqishop.repositories.model.ModelRepository;
import com.app85soft.qiqishop.repositories.order.OrderDetailBatchRepository;
import com.app85soft.qiqishop.repositories.order.OrderDetailRepository;
import com.app85soft.qiqishop.repositories.order.OrderRepository;
import com.app85soft.qiqishop.repositories.stock_batch.StockBatchRepository;
import com.app85soft.qiqishop.repositories.transaction.TransactionRepository;
import com.app85soft.qiqishop.repositories.user.UserRepository;
import com.app85soft.qiqishop.services.BaseService;
import com.app85soft.qiqishop.services.vnpay.VnpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl extends BaseService implements PaymentService {
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final GhnClient ghnClient;
    private final VnpayService vnpayService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final TransactionRepository transactionRepository;
    private final ModelRepository modelRepository;
    private final StockBatchRepository stockBatchRepository;
    private final OrderDetailBatchRepository orderDetailBatchRepository;
    private final SendEmailService sendEmailService;
    private final UserRepository userRepository;

    @Override
    public BaseResponse<CheckOutRes> checkOut(CheckOutReq req) {
        User user = getUser();
        AddressRes address = resolveAddress(req.getAddressId(), user.getId());

        List<CartRes.CartItemRes> items = getCartItems(req.getSelectCartItemId(), user.getId());
        BigDecimal merchandiseTotal = calculateMerchandiseTotal(items);
        BigDecimal shippingFee = calculateShippingFee(items, address);
        BigDecimal totalAmount = merchandiseTotal.add(shippingFee);

        CheckOutRes res = CheckOutRes.builder()
                .items(items)
                .address(address)
                .merchandiseTotal(merchandiseTotal)
                .shippingCost(shippingFee)
                .totalAmount(totalAmount)
                .build();
        return new BaseResponse<>(res);
    }

    @Override
    public BaseResponse<ConfirmCheckOutRes> confirmCheckOut(ConfirmCheckOutReq req) {
        User user = getUser();
        AddressRes address = addressRepository.getAddress(req.getAddressId(), user.getId());
        List<CartRes.CartItemRes> items = getCartItems(req.getSelectCartItemId(), user.getId());

        BigDecimal merchandiseTotal = calculateMerchandiseTotal(items);
        BigDecimal shippingFee = calculateShippingFee(items, address);
        BigDecimal totalAmount = merchandiseTotal.add(shippingFee);

        if (req.getTotalAmount().compareTo(totalAmount) != 0) {
            throw new BusinessException("merchandiseTotal_or_shippingFee_had_change");
        }

        String orderCode = "OD" + System.currentTimeMillis();
        PaymentGateway gateway = req.getPaymentGateway();
        String paymentUrl = null;

        if (gateway == PaymentGateway.CASH) {
            OrderReq orderReq = OrderReq.builder()
                    .userId(user.getId())
                    .addressId(req.getAddressId())
                    .code(orderCode)
                    .totalPrice(totalAmount)
                    .paymentMethod(PaymentMethod.CASH_ON_DELIVERY)
                    .shippingCost(shippingFee)
                    .note(req.getNote())
                    .status(0)
                    .build();

            TransactionReq transactionReq = TransactionReq.builder()
                    .paymentMethod(PaymentMethod.CASH_ON_DELIVERY)
                    .paymentGateway(PaymentGateway.CASH)
                    .status(TransactionStatus.PENDING)
                    .userId(user.getId())
                    .amount(totalAmount)
                    .description("Thanh toán khi nhận hàng cho đơn " + orderCode)
                    .build();

            int orderId = saveOrderAndDetails(orderReq, transactionReq, items);
            String content = sendEmailService.buildInvoiceContent(orderId);

            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setRecipient(user.getEmail());
            emailDetail.setSubject("Đặt hàng thành công !");
            emailDetail.setMsgBody(content);
            sendEmailService.sendSimpleMail(emailDetail);

            for (CartRes.CartItemRes item : items) {
                modelRepository.decreaseStock(item.getModelId(), item.getQuantity());
            }
            List<Integer> cartItemIds = items.stream()
                    .map(CartRes.CartItemRes::getId)
                    .toList();
            cartItemRepository.deleteAllByIdsAndUserId(cartItemIds, user.getId());

        } else if (gateway == PaymentGateway.VNPAY) {
            List<Integer> itemIds = items.stream().map(CartRes.CartItemRes::getId).toList();
            paymentUrl = vnpayService.createOrder(totalAmount, orderCode, itemIds, user.getId(), req.getAddressId(), req.getNote(), shippingFee);
        } else {
            throw new BusinessException("payment_method_invalid");
        }

        ConfirmCheckOutRes res = ConfirmCheckOutRes.builder()
                .orderCode(orderCode)
                .totalAmount(totalAmount)
                .paymentGateway(gateway)
                .paymentUrl(paymentUrl)
                .build();

        return new BaseResponse<>(res);
    }


    @Override
    public void handleVnPaySuccess(String orderCode, int addressId, BigDecimal totalAmount, List<Integer> cartItemIds, int userId, String referenceCode, long payDate, String note, BigDecimal shippingCost) {
        List<CartRes.CartItemRes> items = getCartItems(cartItemIds, userId);

        OrderReq orderReq = OrderReq.builder()
                .userId(userId)
                .addressId(addressId)
                .code(orderCode)
                .totalPrice(totalAmount)
                .paymentMethod(PaymentMethod.BANK_TRANSFER)
                .shippingCost(shippingCost)
                .note(note)
                .status(0)
                .build();

        TransactionReq transactionReq = TransactionReq.builder()
                .referenceCode(referenceCode)
                .userId(userId)
                .amount(totalAmount)
                .paymentGateway(PaymentGateway.VNPAY)
                .payDate(payDate)
                .paymentMethod(PaymentMethod.BANK_TRANSFER)
                .status(TransactionStatus.SUCCESS)
                .build();

        int orderId = saveOrderAndDetails(orderReq, transactionReq, items);
        String content = sendEmailService.buildInvoiceContent(orderId);

        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient(userRepository.getProfileUser(userId).getEmail());
        emailDetail.setSubject("Đặt hàng thành công !");
        emailDetail.setMsgBody(content);
        sendEmailService.sendSimpleMail(emailDetail);
        for(CartRes.CartItemRes item : items) {
            modelRepository.decreaseStock(item.getModelId(), item.getQuantity());
        }
        cartItemRepository.deleteAllByIdsAndUserId(cartItemIds, userId);
    }


    private AddressRes resolveAddress(Integer addressId, int userId) {
        return (addressId != null) ?
                addressRepository.getAddress(addressId, userId) :
                addressRepository.getDefaultAddress(userId);
    }

    private List<CartRes.CartItemRes> getCartItems(List<Integer> itemIds, int userId) {
        List<CartRes.CartItemRes> items = cartItemRepository.getListCartIteam(itemIds, userId);
        if (items == null || items.isEmpty()) {
            throw new BusinessException(Translator.toLocale("no_item_in_cart"));
        }
        return items;
    }

    private BigDecimal calculateMerchandiseTotal(List<CartRes.CartItemRes> items) {
        return items.stream()
                .map(CartRes.CartItemRes::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateShippingFee(List<CartRes.CartItemRes> items, AddressRes address) {
        int totalWeight = items.stream()
                .mapToInt(item -> item.getWeight() * item.getQuantity())
                .sum();

        return BigDecimal.valueOf(ghnClient.calculateShippingFee(Map.of(
                "service_type_id", 2,
                "to_district_id", address.getDistrictGhnId(),
                "to_ward_code", address.getWardGhnCode(),
                "weight", totalWeight
        )));
    }

    private int saveOrderAndDetails (OrderReq orderReq, TransactionReq transactionReq, List<CartRes.CartItemRes> items) {
        Order order = new Order();
        order.setUserId(orderReq.getUserId());
        order.setCode(orderReq.getCode());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(orderReq.getPaymentMethod());
        order.setShippingCost(orderReq.getShippingCost());
        order.setNote(order.getNote());
        order.setTotalPrice(orderReq.getTotalPrice());
        order.setAddressId(orderReq.getAddressId());
        orderRepository.save(order);

        for (CartRes.CartItemRes item : items) {
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(order.getId());
            detail.setModelId(item.getModelId());
            detail.setAmount(item.getQuantity());
            detail.setOriginalPrice(item.getOriginalPrice());
            detail.setFinalPrice(item.getFinalPrice());
            orderDetailRepository.save(detail);

            allocateStockBatches(detail);
        }

        Transactions transaction = new Transactions();
        transaction.setCode(UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        transaction.setReferenceCode(transactionReq.getReferenceCode());
        transaction.setUserId(transactionReq.getUserId());
        transaction.setOrderId(order.getId());
        transaction.setAmount(transactionReq.getAmount());
        transaction.setPaymentGateway(transactionReq.getPaymentGateway());
        transaction.setPaymentMethod(transactionReq.getPaymentMethod());
        transaction.setStatus(transactionReq.getStatus());
        transaction.setDescription("Thanh toán cho đơn hàng " + order.getCode());
        transaction.setPayDate(transactionReq.getPayDate());
        transactionRepository.save(transaction);
        return order.getId();
    }

    private void allocateStockBatches(OrderDetail detail) {
        int amountToAllocate = detail.getAmount();

        List<StockBatch> availableBatches = stockBatchRepository
                .findAvailableBatchesByModelId(detail.getModelId());

        for (StockBatch batch : availableBatches) {
            if (amountToAllocate == 0) break;

            int quantityTaken = Math.min(batch.getQuantityRemaining(), amountToAllocate);

            OrderDetailBatch odb = new OrderDetailBatch();
            odb.setOrderDetailId(detail.getId());
            odb.setStockBatchId(batch.getId());
            odb.setQuantityAllocated(quantityTaken);
            odb.setDeleted(false);
            orderDetailBatchRepository.save(odb);

            batch.setQuantityRemaining(batch.getQuantityRemaining() - quantityTaken);
            stockBatchRepository.save(batch);

            amountToAllocate -= quantityTaken;
        }

        if (amountToAllocate > 0) {
            throw new BusinessException("Số lượng tồn kho không đủ cho model: " + detail.getModelId());
        }
    }

}
