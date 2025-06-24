package com.app85soft.qiqishop.other_service.send_email;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.request.EmailDetail;
import com.app85soft.qiqishop.dto.response.order.OrderRes;
import com.app85soft.qiqishop.entities.order.Order;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.order.OrderDetailRepository;
import com.app85soft.qiqishop.repositories.order.OrderRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class SendEmailServiceImpl implements SendEmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final OrderRepository orderRepository;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendSimpleMail(EmailDetail request) {
        Thread thread = new Thread(() -> {
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper msg = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                msg.setTo(request.getRecipient());
                msg.setSubject(request.getSubject());
                msg.setText(request.getMsgBody(), true);
                msg.setFrom(new InternetAddress(sender, "QiqiShop"));

                javaMailSender.send(mimeMessage);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(Translator.toLocale("label_failed"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });
        thread.start();
    }

    /**
     * Render nội dung HTML từ template Thymeleaf
     */
    @Override
    public String buildInvoiceContent(int orderId) {
        Context context = new Context();
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        OrderRes order = orderRepository.getOrder(orderId);
        context.setVariable("code", order.getCode());
        context.setVariable("userName", order.getUserName());
        context.setVariable("shippingCost", format.format(order.getShippingCost()) + " ₫");
        context.setVariable("total", format.format(order.getTotalPrice()) + " ₫");

        List<Map<String, Object>> items = order.getOrderDetails().stream().map(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("productName", item.getProductName());
            map.put("modelName", item.getModelName());
            map.put("quantity", item.getAmount());
            map.put("unitPrice", format.format(item.getFinalPrice()) + " ₫");
            map.put("totalPrice", format.format(item.getFinalPrice().multiply(BigDecimal.valueOf(item.getAmount()))) + " ₫");

            return map;
        }).collect(Collectors.toList());

        context.setVariable("items", items);

        return templateEngine.process("email/invoice-template", context); // resources/templates/email/invoice-template.html
    }
}
