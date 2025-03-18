package com.app85soft.qiqishop.services.otp;

import com.app85soft.qiqishop.dto.request.otp.SendOtpReq;
import com.app85soft.qiqishop.dto.response.otp.SendOtp;

public interface OtpService {
    SendOtp sendOtpUser (SendOtpReq request);
}
