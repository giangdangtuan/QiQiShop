package com.app85soft.qiqishop.util;

import lombok.extern.log4j.Log4j2;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

@Log4j2
public final class Aes {
    public static byte[] deriveKey(String baseKey, String purpose, int length) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return Arrays.copyOf(digest.digest((baseKey + purpose).getBytes()), length); // Lấy 32 byte đầu
    }

    public static String encrypt(String plaintext, String secretKey) throws Exception {
        // Dẫn xuất khóa AES và HMAC từ secretKey
        byte[] aesKey = deriveKey(secretKey, "AES", 32);
        byte[] hmacKey = deriveKey(secretKey, "HMAC", 32);
        byte[] iv = deriveKey(secretKey, "IV", 16);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Mã hóa AES-256
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec aesKeySpec = new SecretKeySpec(aesKey, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKeySpec, ivSpec);
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

        // Tạo HMAC
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec hmacKeySpec = new SecretKeySpec(hmacKey, "HmacSHA256");
        mac.init(hmacKeySpec);
        byte[] hmac = mac.doFinal(ciphertext);

        // Kết hợp IV, Ciphertext, và HMAC
        byte[] combined = new byte[iv.length + ciphertext.length + hmac.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);
        System.arraycopy(hmac, 0, combined, iv.length + ciphertext.length, hmac.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String encryptedData, String secretKey) throws Exception {
        // Dẫn xuất khóa AES và HMAC từ secretKey
        byte[] aesKey = deriveKey(secretKey, "AES", 32);
        byte[] hmacKey = deriveKey(secretKey, "HMAC", 32);

        // Giải mã Base64
        byte[] combined = Base64.getDecoder().decode(encryptedData);

        // Tách IV, Ciphertext và HMAC
        byte[] iv = Arrays.copyOfRange(combined, 0, 16);
        byte[] ciphertext = Arrays.copyOfRange(combined, 16, combined.length - 32);
        byte[] hmac = Arrays.copyOfRange(combined, combined.length - 32, combined.length);

        // Kiểm tra HMAC
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec hmacKeySpec = new SecretKeySpec(hmacKey, "HmacSHA256");
        mac.init(hmacKeySpec);
        byte[] computedHmac = mac.doFinal(ciphertext);

        if (!Arrays.equals(hmac, computedHmac)) {
            throw new SecurityException("HMAC verification failed");
        }

        // Giải mã AES-256
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec aesKeySpec = new SecretKeySpec(aesKey, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, aesKeySpec, ivSpec);
        byte[] plaintext = cipher.doFinal(ciphertext);

        return new String(plaintext);
    }

}
