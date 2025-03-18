package com.app85soft.qiqishop.services.otp;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.OtpSendPurpose;
import com.app85soft.qiqishop.dto.constant.VerifyStatus;
import com.app85soft.qiqishop.dto.request.EmailDetail;
import com.app85soft.qiqishop.dto.request.otp.SendOtpReq;
import com.app85soft.qiqishop.dto.response.otp.SendOtp;
import com.app85soft.qiqishop.dto.response.zns.ZnsResponse;
import com.app85soft.qiqishop.entities.otp.Otp;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.other_service.send_email.SendEmailServiceImpl;
import com.app85soft.qiqishop.repositories.otp.OtpRepository;
import com.app85soft.qiqishop.repositories.user.UserRepository;
import com.app85soft.qiqishop.services.BaseService;
import com.app85soft.qiqishop.util.Constants;
import com.app85soft.qiqishop.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl extends BaseService implements OtpService {

    @Autowired
    private ResourceLoader resourceLoader;
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final SendEmailServiceImpl sendEmailService;

    @Value("${zns.config.template_id}")
    private int ID_TEMPLATE;
    @Value("${zns.config.oa_id}")
    private String OA_ID;
    @Value("${zns.config.url}")
    private String URL;
    @Value("${zns.config.apikey}")
    private String API_KEY;
    @Value("${spring.mail.username}")
    private String EMAIL_SENDER;
    @Value("${spring.mail.password}")
    private String EMAIL_PASS;
    @Value("${spring.mail.port}")
    private String EMAIL_PORT;

    @Override
    @Transactional
    public SendOtp sendOtpUser(SendOtpReq request) {
        User user = userRepository.getUserByPhone(request.getPhone());
        if (request.getType() == null || request.getPurpose() == null) {
            throw new BusinessException(Translator.toLocale("invalid_request"), HttpStatus.BAD_REQUEST);
        }
        if (user != null && request.getPurpose().equals(OtpSendPurpose.REGISTRATION)) {
            throw new BusinessException(Translator.toLocale("phone_already_exists"), HttpStatus.BAD_REQUEST);
        }
        if (user == null && request.getPurpose().equals(OtpSendPurpose.PASSWORD_RESET)) {
            throw new BusinessException(Translator.toLocale("phone_no_exists"), HttpStatus.BAD_REQUEST);
        }
        return sendOtp(request, user);
    }

    private SendOtp sendOtp(SendOtpReq request, Object object) {

        otpRepository.updateStatusVerify(request);

        Otp otp = new Otp();
        otp.setPhone(request.getPhone());
        otp.setEmail(request.getEmail());
        otp.setSendType(request.getType());
        otp.setOtp(Util.randomString(6, Constants.DIGITS));
        otp.setStatus(VerifyStatus.VERIFY_PENDING);
        otpRepository.save(otp);

        SendOtp result = new SendOtp();
        switch (request.getType()) {
            case ZNS:
                sendZns(request, otp.getOtp());
                result.setId(otp.getId());
                result.setPhone(otp.getPhone());
                break;
            case SMS:
                // Call func send otp with SMS
//                Phục vụ test
                result.setId(otp.getId());
                result.setOtp(otp.getOtp());
                result.setPhone(otp.getPhone());
                break;
            case EMAIL:
                sendEmail(request, otp.getOtp(), request.getPurpose());
                result.setId(otp.getId());
                result.setEmail(otp.getEmail());
                break;
            default:
                throw new BusinessException(Translator.toLocale("invalid_request"), HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    private void sendZns(SendOtpReq request, String otp) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> templateDate = new HashMap<>();
        templateDate.put("otp", otp);

        Map<String, Object> payload = new HashMap<>();
        payload.put("phone", request.getPhone());
        payload.put("oa_id", OA_ID);
        payload.put("template_id", ID_TEMPLATE);
        payload.put("template_data", templateDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(URL, entity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            ZnsResponse znsResponse = mapper.readValue(response.getBody(), ZnsResponse.class);
            if (znsResponse.getErrorCode() != 0) {
                throw new BusinessException(znsResponse.getErrorMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new BusinessException(Translator.toLocale("send_otp_error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private void sendEmail(SendOtpReq request, String otp, OtpSendPurpose purpose) {
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient(request.getEmail());
        if (purpose == OtpSendPurpose.REGISTRATION) {
            emailDetail.setSubject(Translator.toLocale("send_otp_register").replace("[[${timeSend}]]", Util.convertDateToString(new Date(), "yyyy-MM-dd hh:mm:ss")));
            emailDetail.setMsgBody(readTemplate("send_otp_register.html").replace("[[${otpCode}]]", otp));
        } else {
            emailDetail.setSubject(Translator.toLocale("send_otp_forgot_password").replace("[[${timeSend}]]", Util.convertDateToString(new Date(), "yyyy-MM-dd hh:mm:ss")));
            emailDetail.setMsgBody(readTemplate("send_otp_forgot_password.html").replace("[[${otpCode}]]", otp));
        }
        sendEmailService.sendSimpleMail(emailDetail);
    }

    private String readTemplate(String templateName) {
        Resource resource = resourceLoader.getResource("classpath:templates/" + templateName);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
