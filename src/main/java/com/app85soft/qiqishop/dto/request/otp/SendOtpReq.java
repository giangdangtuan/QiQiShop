package com.app85soft.qiqishop.dto.request.otp;

import com.app85soft.qiqishop.dto.constant.OtpSendPurpose;
import com.app85soft.qiqishop.dto.constant.SendType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendOtpReq {
    String phone;
    String email;
    @NotNull
    SendType type;
    @NotNull
    OtpSendPurpose purpose;

    @AssertTrue(message = "Either phone or email must be provided")
    public boolean isValid() {
        return phone != null || email != null;
    }
}
