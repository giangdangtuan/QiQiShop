package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.request.EmailDetail;
import com.app85soft.qiqishop.other_service.send_email.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailTestController {

    private final SendEmailService sendEmailService;

    @GetMapping("/test-email")
    public ResponseEntity<String> testSendEmail() {
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient("hami2502@gmail.com");
        emailDetail.setSubject("Quà siêu hot từ QiqiShop !!!");
        emailDetail.setMsgBody("<h1>Chúc mừng bạn</h1><p>Đã được tặng 1 buổi giao lưu cầu lông vào tối nay ! Nếu không tham gia chứng tỏ bạn sợ</p>");
        sendEmailService.sendSimpleMail(emailDetail);
        return ResponseEntity.ok("Đã gửi email!");
    }
}

