package com.app85soft.qiqishop.services.vnpay;

import com.app85soft.qiqishop.configuration.VnpayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VnpayService {
    private final VnpayConfig config;

    public String createOrder(BigDecimal total, String orderCode, List<Integer> cartItemIds, int userId, int addressId, String note, BigDecimal shippingCost) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TmnCode = config.vnp_TmnCode;
        String orderType = "order-type";

        // Ghép thông tin orderInfo
        String cartItemIdsStr = cartItemIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String orderInfo = String.format("orderCode=%s|cart=%s|userId=%d|addressId=%d|note=%s|shippingCost=%f",
                orderCode, cartItemIdsStr, userId, addressId, note, shippingCost);

        // Build parameter
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(total.multiply(BigDecimal.valueOf(100)).intValue()));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", orderCode);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", config.vnp_Returnurl);
        // Không cần truyền IP nếu không chắc chắn (tránh lỗi 03)
        // vnp_Params.put("vnp_IpAddr", "127.0.0.1");

        // Tạo create/expire time
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Build hashData và query đúng chuẩn
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                try {
                    String encodedName = URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString());
                    String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString());

                    hashData.append(fieldName).append('=').append(encodedValue);
                    query.append(encodedName).append('=').append(encodedValue);

                    if (i < fieldNames.size() - 1) {
                        hashData.append('&');
                        query.append('&');
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        String vnp_SecureHash = config.hmacSHA512(config.vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        String paymentUrl = config.vnp_PayUrl + "?" + query;

        // 🔍 Log phục vụ debug
        System.out.println("🔐 VNPay hashData: " + hashData);
        System.out.println("🔑 SecureHash: " + vnp_SecureHash);
        System.out.println("🔗 Payment URL: " + paymentUrl);

        return paymentUrl;
    }
}
