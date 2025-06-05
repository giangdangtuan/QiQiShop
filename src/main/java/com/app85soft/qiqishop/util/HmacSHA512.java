package com.app85soft.qiqishop.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public final class HmacSHA512 {
    private static final String HMAC_SHA512_ALGORITHM = "HmacSHA512";

    public static String encrypt(String message, String secretKey) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA512_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA512_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

            // Trả về HEX chuỗi thay vì Base64 như SHA256
            StringBuilder sb = new StringBuilder();
            for (byte b : rawHmac) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC-SHA512", e);
        }
    }
}
