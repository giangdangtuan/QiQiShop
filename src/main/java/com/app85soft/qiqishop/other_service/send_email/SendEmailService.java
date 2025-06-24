package com.app85soft.qiqishop.other_service.send_email;

import com.app85soft.qiqishop.dto.request.EmailDetail;

public interface SendEmailService {
    void sendSimpleMail(EmailDetail details);

    String buildInvoiceContent(int orderId);
}

