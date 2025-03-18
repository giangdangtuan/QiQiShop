package com.app85soft.qiqishop.repositories.otp;

import com.app85soft.qiqishop.dto.request.otp.SendOtpReq;
import com.app85soft.qiqishop.entities.otp.Otp;

public interface OtpRepositoryCustom {
    //    Optional<Otp> sendOtp(SendOtpReq request);
    void updateStatusVerify (SendOtpReq request);
    Otp findSessionAuth(String phone, String email, String otp);
}
