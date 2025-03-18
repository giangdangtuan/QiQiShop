package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.request.auth.ForgotPasswordReq;
import com.app85soft.qiqishop.dto.request.auth.UserLoginReq;
import com.app85soft.qiqishop.dto.request.otp.SendOtpReq;
import com.app85soft.qiqishop.dto.request.user.RegisterUser;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.otp.SendOtp;
import com.app85soft.qiqishop.dto.response.user.UserDetailRes;
import com.app85soft.qiqishop.services.otp.OtpService;
import com.app85soft.qiqishop.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final OtpService otpService;

    @Operation(summary = "Login")
    @PostMapping("v1/auth/login")
    public ResponseEntity<BaseResponse<UserDetailRes>> loginUser(@RequestBody @Valid UserLoginReq request) {
        return ResponseEntity.ok(new BaseResponse<>(userService.login(request)));
    }

    @Operation(summary = "Register")
    @PostMapping("v1/auth/register")
    public ResponseEntity<BaseResponse<UserDetailRes>> registerUser(@RequestBody @Valid RegisterUser request) {
        return ResponseEntity.ok(new BaseResponse<>(userService.register(request)));
    }

    @Operation(summary = "Forgot password")
    @PostMapping("v1/auth/forgot-password")
    public ResponseEntity<BaseResponse<String>> forgotPassword(@RequestBody @Valid ForgotPasswordReq request) {
        return ResponseEntity.ok(new BaseResponse<>(userService.forgotPassword(request)));
    }

    @Operation(summary = "Send OTP forgot password")
    @PostMapping("v1/auth/send-otp")
    public ResponseEntity<BaseResponse<SendOtp>> sendOtpUser(@RequestBody @Valid SendOtpReq request) {
        return ResponseEntity.ok(new BaseResponse<>(otpService.sendOtpUser(request)));
    }
}
