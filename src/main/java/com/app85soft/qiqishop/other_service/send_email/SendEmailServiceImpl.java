package com.app85soft.qiqishop.other_service.send_email;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.request.EmailDetail;
import com.app85soft.qiqishop.exceptions.BusinessException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Data
public class SendEmailServiceImpl implements SendEmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendSimpleMail(EmailDetail request) {
        Thread thread = new Thread(() -> {
            try {
                final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
                final MimeMessageHelper msg = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                msg.setTo(request.getRecipient());
                msg.setSubject(request.getSubject());
                msg.setText(request.getMsgBody(), true);
                msg.setFrom(new InternetAddress(sender, "MyID"));
                javaMailSender.send(mimeMessage);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(Translator.toLocale("label_failed"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });
        thread.start();
    }
}

